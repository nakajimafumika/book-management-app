package manpowergroup.jp.bookSystem.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookReserveDto {
	private List<String> reservedDates; // 既に予約されている日付
	private List<String> loanPeriodDates; // 貸出されている日付
	private boolean isOverReserveLimit; // 予約の制限（3冊まで）を超えているかどうか
	private LocalDate reservesDate;// 予約開始日
	private LocalDate returnDueDate;// 予約終了日
}
