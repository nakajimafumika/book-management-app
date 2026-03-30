package manpowergroup.jp.bookSystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import manpowergroup.jp.bookSystem.dto.BookListDto;
import manpowergroup.jp.bookSystem.repository.custom.BookListRepository;

@Service
@RequiredArgsConstructor // final フィールドに対して自動でコンストラクタを生成
public class BookListService {

	private final BookListRepository bookListRepository;

	// 本の一覧を取得する
	public List<BookListDto> getBookList() {
		return bookListRepository.getBookList();
	}
}
