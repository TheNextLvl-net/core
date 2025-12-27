package core.paper.cache;

import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class SimplePlayerCache implements PlayerCache {
    private static final Path playerData = Objects.requireNonNull(
            Bukkit.getWorld(Key.key("minecraft", "overworld"))
    ).getWorldPath().resolve("playerdata");

    private @Unmodifiable Set<UUID> storedPlayers = new HashSet<>();
    private Instant nextUpdate = Instant.now();
    private final Duration retention;

    public SimplePlayerCache(Duration retention) {
        this.retention = retention;
    }

    @Override
    public Duration getRetention() {
        return retention;
    }

    @Override
    public Stream<UUID> updateStoredPlayers(@Nullable Function<Stream<UUID>, Stream<UUID>> filter) {
        try (var files = Files.list(playerData)) {
            var stream = files
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".dat"))
                    .map(name -> name.substring(0, name.length() - 4))
                    .map(s -> {
                        try {
                            return UUID.fromString(s);
                        } catch (IllegalArgumentException ignored) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull);

            if (filter != null) stream = filter.apply(stream);

            this.nextUpdate = Instant.now().plus(retention);
            this.storedPlayers = stream.collect(Collectors.toUnmodifiableSet());
            return storedPlayers.stream();
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    @Override
    public Optional<Stream<UUID>> getStoredPlayers() {
        var now = Instant.now();
        if (now.isBefore(nextUpdate)) return Optional.of(storedPlayers.stream());

        this.nextUpdate = now.plus(retention);
        this.storedPlayers = Set.of();

        return Optional.empty();
    }

    @Override
    public Stream<UUID> getOrUpdateStoredPlayers(@Nullable Function<Stream<UUID>, Stream<UUID>> filter) {
        return getStoredPlayers().orElseGet(() -> updateStoredPlayers(filter));
    }
}
