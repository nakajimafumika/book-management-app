package manpowergroup.jp.bookSystem.repository.custom;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;

import manpowergroup.jp.bookSystem.dto.BookLoanDto;

@Repository
public class BookLoanRepository {
	@PersistenceContext
	private EntityManager entityManager;

	public BookLoanDto getReservedDates(String managementId, String oid) {

		// ストアドGetReservedDatesの呼出し
		StoredProcedureQuery query = entityManager
				.createStoredProcedureQuery("GetReservedDates");

		query.registerStoredProcedureParameter("in_management_id", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("in_oid", String.class, ParameterMode.IN);

		query.setParameter("in_management_id", managementId);
		query.setParameter("in_oid", oid);

		List<Object[]> resultSet = query.getResultList();

		List<String> otherDates = resultSet.stream()
				.filter(row -> Boolean.FALSE.equals(row[1]))
				.map(row -> ((Date) row[0]).toLocalDate().toString())
				.collect(Collectors.toList());

		List<String> loginDates = resultSet.stream()
				.filter(row -> Boolean.TRUE.equals(row[1]))
				.map(row -> ((Date) row[0]).toLocalDate().toString())
				.collect(Collectors.toList());

		BookLoanDto dto = new BookLoanDto();
		dto.setOtherReservedDates(otherDates);
		dto.setLoginuserReservedDates(loginDates);
		return dto;
	}

}