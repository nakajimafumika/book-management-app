package manpowergroup.jp.bookSystem.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import manpowergroup.jp.bookSystem.entity.BookListsEntity;
import manpowergroup.jp.bookSystem.form.BookRegisterForm;
import manpowergroup.jp.bookSystem.repository.jpa.BookListsJpaRepository;
import manpowergroup.jp.bookSystem.validation.FileValidator;

@Service
public class BookRegisterService {

	private final BookListsJpaRepository bookListsJpaRepository;

	@Value("${upload.dir}")
	private String uploadDir;

	public BookRegisterService(BookListsJpaRepository bookListsJpaRepository) {
		this.bookListsJpaRepository = bookListsJpaRepository;
	}

	// 書籍登録
	@Transactional
	public void registerBook(BookRegisterForm request) throws IOException {
		if (bookListsJpaRepository.existsByManagementId(request.getManagementId())) {
			throw new DuplicateKeyException("管理IDは既に登録されています");
		}

		BookListsEntity entity = new BookListsEntity();
		entity.setTitle(request.getTitle());
		entity.setAuthor(request.getAuthor());
		entity.setPublisher(request.getPublisher());
		entity.setPublicationDate(request.getPublicationDate());
		entity.setManagementId(request.getManagementId());
		entity.setIsbnCode(request.getIsbnCode());

		// 画像があれば保存してファイル名をセット
		if (request.getUploadBookImage() != null && !request.getUploadBookImage().isEmpty()) {

			// 画像ファイルのバリデーション
			FileValidator.validateImageFile(request.getUploadBookImage());

			// 保存先ディレクトリの作成
			Path dirPath = Paths.get(uploadDir);
			Files.createDirectories(dirPath);

			// ファイル名加工
			String fileName = System.currentTimeMillis() + "_" + request.getUploadBookImage().getOriginalFilename();

			// ファイルの保存
			Path path = dirPath.resolve(fileName);
			request.getUploadBookImage().transferTo(path.toFile());

			entity.setBookImageName(fileName);
		}

		// 書籍情報＋画像ファイル名をまとめて保存
		bookListsJpaRepository.save(entity);
	}

	// 本の情報をファイルから保存
	public void registerBooksFromFile(MultipartFile file) throws IOException {
		String filename = file.getOriginalFilename();
		List<String[]> rows;

		//CSVファイルかEXCELかを判断
		if (filename.endsWith(".csv")) {
			rows = readCsv(file);
		} else if (filename.endsWith(".xlsx") || filename.endsWith(".xls")) {
			rows = readExcel(file);
		} else {
			throw new IllegalArgumentException("対応していないファイル形式です");
		}

		// 共通処理：行データをBookRegisterFormに変換して登録
		convertAndRegister(rows);
	}

	/** CSV読み込み */
	private List<String[]> readCsv(MultipartFile file) throws IOException {
		List<String[]> rows = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			String line;
			boolean isFirstLine = true;
			while ((line = br.readLine()) != null) {
				if (isFirstLine) { // ヘッダー行スキップ
					isFirstLine = false;
					continue;
				}
				rows.add(line.split(","));
			}
		}
		return rows;
	}

	/** Excel読み込み */
	private List<String[]> readExcel(MultipartFile file) throws IOException {
		List<String[]> rows = new ArrayList<>();
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			boolean isFirstRow = true;
			for (Row row : sheet) {
				if (isFirstRow) { // ヘッダー行スキップ
					isFirstRow = false;
					continue;
				}
				List<String> values = new ArrayList<>();
				for (Cell cell : row) {
					values.add(cell.toString());
				}
				rows.add(values.toArray(new String[0]));
			}
		}
		return rows;
	}

	/** 共通処理：行データをBookRegisterFormに変換して登録 */
	private void convertAndRegister(List<String[]> rows) {
		for (String[] values : rows) {
			BookRegisterForm form = new BookRegisterForm();
			form.setManagementId(values[1]); // 2列目
			form.setIsbnCode(values[0]); // 1列目 
			form.setTitle(values[2]); // 3列目
			form.setAuthor(values[3]); // 4列目
			form.setPublisher(values[4]); // 5列目
			form.setPublicationDate(values[5]); // 6列目

			//registerBook(form);
		}
	}
}
