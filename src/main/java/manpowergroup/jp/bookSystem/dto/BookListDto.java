package manpowergroup.jp.bookSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookListDto {
	private Integer id;
	private String managementId;
	private String title;
	private String author;
	private String status;
	private Double review;

}
