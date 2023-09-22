package core.i18n.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(fluent = true, chain = true)
public class ComponentBundle {
    private final Map<Locale, ResourceBundle> files = new HashMap<>();
    private final Function<Audience, Locale> localeFunction;
    private MiniMessage miniMessage = MiniMessage.miniMessage();
    private @Nullable Locale fallback;

    /**
     * Register a new bundle
     *
     * @param baseName the base name of the resource bundle
     * @param locale   the locale for which the resource bundle is desired
     * @return the component bundle
     */
    public ComponentBundle register(String baseName, Locale locale) {
        files.put(locale, ResourceBundle.getBundle(baseName, locale, UTF8ResourceBundleControl.get()));
        return this;
    }

    /**
     * Get a deserialized component from a locale and property key
     *
     * @param key          the key to get the input string from
     * @param locale       the locale to get the input string from
     * @param tagResolvers a series of tag resolvers to apply extra tags from, last specified taking priority
     * @return the {@link MiniMessage#deserialize(String, TagResolver...) deserialized} component
     */
    public Component component(Locale locale, String key, TagResolver... tagResolvers) {
        var resourceBundle = files.getOrDefault(locale, files.get(fallback()));
        var string = resourceBundle.getString(key);
        return miniMessage.deserialize(string, tagResolvers);
    }

    public void sendMessage(Audience audience, String key, TagResolver... tagResolvers) {
        audience.sendMessage(component(localeFunction.apply(audience), key, tagResolvers));
    }
}
