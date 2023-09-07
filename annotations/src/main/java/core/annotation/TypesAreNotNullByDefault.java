package core.annotation;

import org.jetbrains.annotations.NotNull;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NotNull
@TypeQualifierDefault({ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TypesAreNotNullByDefault {
}
