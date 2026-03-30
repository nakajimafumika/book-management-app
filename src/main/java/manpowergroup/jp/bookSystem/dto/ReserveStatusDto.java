package manpowergroup.jp.bookSystem.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class ReserveStatusDto {
	private Integer reserveId;
	private String title; // 書籍名
	private String reservesDate; // 予約開始日

	public ReserveStatusDto(Integer reserveId, String title, LocalDate reservesDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		this.reserveId = reserveId;
		this.title = title;
		this.reservesDate = reservesDate.format(formatter);
	}
}
