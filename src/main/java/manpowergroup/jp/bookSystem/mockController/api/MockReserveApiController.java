package manpowergroup.jp.bookSystem.mockController.api;

import java.time.LocalDate;
import java.util.ArrayList;
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
public class MockReserveApiController {
	//予約情報取得用コントローラー
	@GetMapping("/reserve-info")
	public Map<String, Object> getMockReserveInfo(@RequestParam String managementId) {
		List<String> reservedDates = new ArrayList<>();
		List<Map<String, String>> reservations = List.of(
				Map.of("start", "2025-10-12", "end", "2025-10-14"),
				Map.of("start", "2025-10-18", "end", "2025-10-20"));

		for (Map<String, String> reservation : reservations) {
			LocalDate start = LocalDate.parse(reservation.get("start"));
			LocalDate end = LocalDate.parse(reservation.get("end"));
			while (!start.isAfter(end)) {
				reservedDates.add(start.toString());
				start = start.plusDays(1);
			}
		}

		String lendingStartDate = "2025-09-05";
		String plannedReturnDate = "2025-09-20";
		List<String> loanPeriodDates = new ArrayList<>();
		LocalDate start = LocalDate.parse(lendingStartDate);
		LocalDate end = LocalDate.parse(plannedReturnDate);
		while (!start.isAfter(end)) {
			loanPeriodDates.add(start.toString());
			start = start.plusDays(1);
		}

		String baseDate = plannedReturnDate;
		LocalDate startDate = LocalDate.parse(baseDate).plusDays(1);
		while (reservedDates.contains(startDate.toString())) {
			startDate = startDate.plusDays(1);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("reservationStartDate", startDate.toString());
		response.put("reservedDates", reservedDates);
		response.put("loanPeriodDates", loanPeriodDates);
		response.put("oid", "user-001");
		return response;
	}

	//予約登録用コントローラー
	@PostMapping("/reserve-register")
	public ResponseEntity<?> confirmMockReservation(@RequestBody Map<String, String> request) {
		String managementId = request.get("managementId");
		String reserveDate = request.get("reservesDate");
		String returnDueDate = request.get("returnDueDate");

		if (managementId == null || reserveDate == null) {
			return ResponseEntity.badRequest().body("managementIdとreservesDateは必須です");
		}

		// モックなのでDB登録は省略。実際は重複チェック＋INSERTが必要
		Map<String, String> response = new HashMap<>();
		response.put("message", "予約が完了しました");
		response.put("reservedDate", reserveDate);
		return ResponseEntity.ok(response);
	}

}