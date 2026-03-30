package manpowergroup.jp.common.exception;

import java.sql.SQLException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class HtmlExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public String handleBusinessError(BusinessException ex, Model model) {
		model.addAttribute("errorType", "業務エラー");
		model.addAttribute("message", ex.getMessage());
		return "/common/error"; // error.html に遷移
	}

	@ExceptionHandler(SQLException.class)
	public String handleDatabaseError(SQLException ex, Model model) {
		model.addAttribute("errorType", "DBエラー");
		model.addAttribute("message", "現在、システムが一時的にご利用いただけません。復旧までしばらくお待ちください。");
		return "/common/error";
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public String handleNotFound(NoResourceFoundException ex, Model model) {
		model.addAttribute("errorType", "URL指定ミス");
		model.addAttribute("message", "お探しのページが見つかりませんでした。入力されたURLが間違っているか、ページが削除された可能性があります。");
		return "/common/error";
	}

	@ExceptionHandler(Exception.class)
	public String handleOtherErrors(Exception ex, Model model) throws Exception {
		//if (ex instanceof NoResourceFoundException nrfe) {

		//org.springframework.web.servlet.resource.NoResourceFoundException nrfe = (org.springframework.web.servlet.resource.NoResourceFoundException) ex;

		// 404 はここで処理せず、Spring Boot のエラーページに任せる
		//throw nrfe;
		//}

		model.addAttribute("errorType", "その他");
		model.addAttribute("message", "予期せぬエラーが発生しました。");
		return "common/error";
	}

}
