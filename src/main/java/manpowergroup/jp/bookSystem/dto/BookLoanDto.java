package manpowergroup.jp.bookSystem.dto;

import java.util.List;

import lombok.Data;

@Data
public class BookLoanDto {
	private List<String> otherReservedDates;// ログインユーザー以外の予約日
	private List<String> loginuserReservedDates;// ログインユーザーの予約日
	private boolean isOverLoanLimit;// 貸出の制限（5冊まで）を超えているかどうか

}
