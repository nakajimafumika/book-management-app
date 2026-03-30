package manpowergroup.jp.bookSystem.repository.custom;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;

import manpowergroup.jp.bookSystem.dto.BookReserveDto;

@Repository
public class BookReserveRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public BookReserveDto getReserveAndLoanDates(String managementId) {

		// ストアドReserveAndLoanDates呼び出し
		StoredProcedureQuery query = entityManager
				.createStoredProcedureQuery("GetReserveAndLoanDates");

		query.registerStoredProcedureParameter("in_management_id", String.class, ParameterMode.IN);
		query.setParameter("in_management_id", managementId);
		//query.execute();

		// 一時テーブルから結果取得
		List<Object[]> resultSet = query.getResultList();

		List<String> reservedDates = resultSet.stream()
				.filter(row -> "reserve".equalsIgnoreCase((String) row[1]))
				.map(row -> ((Date) row[0]).toLocalDate().toString())
				.collect(Collectors.toList());

		List<String> loanPeriodDates = resultSet.stream()
				.filter(row -> "loan".equalsIgnoreCase((String) row[1]))
				.map(row -> ((Date) row[0]).toLocalDate().toString())
				.collect(Collectors.toList());

		BookReserveDto dto = new BookReserveDto();
		dto.setReservedDates(reservedDates);
		dto.setLoanPeriodDates(loanPeriodDates);
		return dto;
	}
}
