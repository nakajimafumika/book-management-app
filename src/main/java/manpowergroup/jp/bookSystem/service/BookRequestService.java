package manpowergroup.jp.bookSystem.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import manpowergroup.jp.bookSystem.entity.BookRequestsEntity;
import manpowergroup.jp.bookSystem.form.BookRequestForm;
import manpowergroup.jp.bookSystem.repository.jpa.BookRequestJpaRepository;
import manpowergroup.jp.common.entity.MUser;
import manpowergroup.jp.common.repository.MUserRepository;

@Service
public class BookRequestService {

	private final BookRequestJpaRepository bookRequestJpaRepository;
	private final MUserRepository mUserRepository;

	public BookRequestService(BookRequestJpaRepository bookRequestJpaRepository, MUserRepository mUserRepository) {
		this.bookRequestJpaRepository = bookRequestJpaRepository;
		this.mUserRepository = mUserRepository;
	}

	public void registerBookRequest(BookRequestForm request, String oid) {

		MUser user = mUserRepository.findByOid(oid)
				.orElseThrow(() -> new IllegalArgumentException("ユーザーOIDが存在しません: " + oid));

		BookRequestsEntity entity = new BookRequestsEntity();
		entity.setUser(user);
		entity.setRequestDate(LocalDate.now()); // サーバー側で設定
		entity.setIsbnCode(request.getIsbnCode());
		entity.setTitle(request.getTitle());
		entity.setAuthor(request.getAuthor());
		entity.setBookUrl(request.getUrl());
		entity.setMemo(request.getMemo());

		// DBに保存
		bookRequestJpaRepository.save(entity);
	}
}
