package manpowergroup.jp.bookSystem.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PublicationDateValidator.class)
public @interface ValidPublicationDate {
	String message() default "日付が不正です。YYYY-MM-DD または YYYY/MM または YYYY/MM/DD 形式で入力してください";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
