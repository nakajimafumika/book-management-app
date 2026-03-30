package manpowergroup.jp.bookSystem.validation;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 *「出版日フィールドに付けた @ValidPublicationDate アノテーションが有効かどうか」を判定する処理を持つ
 *実際のチェックは isValid メソッドで行われる
 *アノテーション自体は別ファイルで @interface ValidPublicationDate として定義済みで、このクラスはその裏側のロジック
 * 
 */
public class PublicationDateValidator implements ConstraintValidator<ValidPublicationDate, String> {

	private static final String[] PATTERNS = {
			"\\d{4}-\\d{2}-\\d{2}", // YYYY-MM-DD
			"\\d{4}/\\d{2}/\\d{2}", // YYYY/MM/DDs
			"\\d{4}/\\d{2}", // YYYY/MM
			"\\d{4}-\\d{2}" // YYYY-MM
	};

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		// 拡張 for 文 (要素の型 変数名 : 配列)
		for (String pattern : PATTERNS) {
			if (value.matches(pattern)) {
				try {
					// "/" を "-" に統一してパース
					String normalized = value.replace("/", "-");

					if (pattern.equals("\\d{4}-\\d{2}-\\d{2}") || pattern.equals("\\d{4}/\\d{2}/\\d{2}")) {
						LocalDate.parse(normalized, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					} else {
						YearMonth.parse(normalized, DateTimeFormatter.ofPattern("yyyy-MM"));
					}
					return true; // パース成功 → 妥当な日付

				} catch (DateTimeParseException e) {
					return false; // パース失敗 → 不正な日付
				}
			}
		}
		return false; // どのパターンにも一致しない
	}
}