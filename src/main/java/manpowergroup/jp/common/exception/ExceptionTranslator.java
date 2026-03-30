package manpowergroup.jp.common.exception;

import java.sql.SQLException;

import org.springframework.orm.jpa.JpaSystemException;

public class ExceptionTranslator {
	public static RuntimeException translate(Throwable ex) {
		if (ex instanceof JpaSystemException jpaEx) {
			Throwable cause = jpaEx.getRootCause();
			if (cause instanceof SQLException sqlEx && "45000".equals(sqlEx.getSQLState())) {
				return new BusinessException(sqlEx.getMessage());
			}
		}
		return new RuntimeException("予期せぬ例外", ex);
	}
}
