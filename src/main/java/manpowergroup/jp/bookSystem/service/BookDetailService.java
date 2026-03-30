package manpowergroup.jp.bookSystem.service;

import org.springframework.stereotype.Service;

import manpowergroup.jp.bookSystem.dto.BookDetailDto;
import manpowergroup.jp.bookSystem.repository.custom.BookDetailRepository;

@Service
public class BookDetailService {

	private final BookDetailRepository bookDetailRepository;

	// コンストラクタ
	public BookDetailService(BookDetailRepository bookDetailRepository) {
		this.bookDetailRepository = bookDetailRepository;
	}

	public BookDetailDto getBookDetailById(Integer id) {
		return bookDetailRepository.getBookDetailById(id);
	}

}