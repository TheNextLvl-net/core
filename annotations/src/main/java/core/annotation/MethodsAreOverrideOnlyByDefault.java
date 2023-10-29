package core.annotation;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ApiStatus.OverrideOnly
@TypeQualifierDefault({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodsAreOverrideOnlyByDefault {
}
