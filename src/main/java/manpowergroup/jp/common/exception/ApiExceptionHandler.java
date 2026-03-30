package manpowergroup.jp.common.exception;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Order(2)
public class ApiExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Map<String, String>> handleBusinessError(BusinessException ex) {
		Map<String, String> body = new HashMap<>();
		body.put("errorType", "業務エラー");
		body.put("message", ex.getMessage());
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		Map<String, String> body = new HashMap<>();
		body.put("errorType", "入力値エラー");

		if (ex.getCause() instanceof DuplicateKeyException) {
			body.put("message", "管理IDは既に登録されています");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
		}

		body.put("message", "入力値を修正してください。");
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Map<String, String>> handleDatabaseError(SQLException ex) {
		Map<String, String> body = new HashMap<>();
		body.put("errorType", "DBエラー");
		body.put("message", "現在、システムが一時的にご利用いただけません。復旧までしばらくお待ちください。");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Map<String, String>> handleNotFound(NoHandlerFoundException ex) {
		Map<String, String> body = new HashMap<>();
		body.put("errorType", "URL指定ミス");
		body.put("message", "お探しのページが見つかりませんでした。入力されたURLが間違っているか、ページが削除された可能性があります。");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

}
