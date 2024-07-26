package core.paper.version;

import core.annotation.FieldsAreNotNullByDefault;
import core.annotation.MethodsReturnNotNullByDefault;
import core.annotation.ParametersAreNotNullByDefault;
import core.annotation.TypesAreNotNullByDefault;
import core.version.HangarVersionChecker;
import core.version.Version;
import me.mrafonso.hangar4j.impl.Platform;
import me.mrafonso.hangar4j.impl.version.HangarVersion;
import org.bukkit.Bukkit;

/**
 * The PaperHangarVersionChecker class is an abstract class that provides methods
 * for retrieving and checking the latest supported version of a specific plugin in a Paper Hangar project.
 *
 * @param <V> the type parameter for the version
 */
@TypesAreNotNullByDefault
@FieldsAreNotNullByDefault
@MethodsReturnNotNullByDefault
@ParametersAreNotNullByDefault
public abstract class PaperHangarVersionChecker<V extends Version> extends HangarVersionChecker<V> {
    public PaperHangarVersionChecker(String slug) {
        super(slug);
    }

    @Override
    public boolean isSupported(HangarVersion version) {
        return version.platformDependencies().get(Platform.PAPER)
                .contains(Bukkit.getMinecraftVersion());
    }
}
