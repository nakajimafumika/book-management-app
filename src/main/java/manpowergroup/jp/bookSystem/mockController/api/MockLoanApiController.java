package manpowergroup.jp.bookSystem.mockController.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("mock")
@RequestMapping("/api")
public class MockLoanApiController {

	/**
	 * Flatpickr用の貸出可能日取得モックAPI（予約情報付き）
	 * @param id 書籍ID（クエリパラメータ）
	 * @return 貸出可能日情報＋予約情報
	 */
	@GetMapping("/loan-info")
	public ResponseEntity<?> getLoanInfo(@RequestParam String managementId, @RequestParam String oid) {

		// 仮の予約者情報
		String reservedOid = "test";
		boolean isReservationUser = oid.equals(reservedOid);

		// ログインユーザーが予約者の場合の予約日（例：10/01〜10/05）
		List<Map<String, String>> loginuserReservedDates = isReservationUser
				? List.of(
						Map.of("date", "2025-10-01"),
						Map.of("date", "2025-10-02"),
						Map.of("date", "2025-10-03"),
						Map.of("date", "2025-10-04"),
						Map.of("date", "2025-10-05"))
				: List.of();

		// 他ユーザーの予約日（例：10/10〜10/14）
		List<String> otherReservedDates = List.of(
				"2025-10-10",
				"2025-10-11",
				"2025-10-12",
				"2025-10-13",
				"2025-10-14");

		Map<String, Object> response = new HashMap<>();
		response.put("managementId", managementId);
		response.put("oid", "test");
		response.put("isReservationUser", isReservationUser);
		response.put("loginuserReservedDates", loginuserReservedDates);
		response.put("otherReservedDates", otherReservedDates);

		return ResponseEntity.ok(response);
	}

	/**
	 * 貸出期間登録用モックAPI（最小構成）
	 */
	@PostMapping("/loan-register")
	public ResponseEntity<?> registerLoanPeriod(@RequestBody Map<String, String> request) {
		Map<String, String> response = new HashMap<>();
		response.put("message", "貸出期間の登録が完了しました");
		response.putAll(request);
		return ResponseEntity.ok(response);
	}
}
