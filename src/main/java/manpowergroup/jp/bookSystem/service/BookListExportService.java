package manpowergroup.jp.bookSystem.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import manpowergroup.jp.bookSystem.dto.BookListDto;

@Service
public class BookListExportService {

	private final BookListService bookListService; // 書籍一覧取得用の既存サービス

	public BookListExportService(BookListService bookListService) {
		this.bookListService = bookListService;
	}

	// 書籍一覧を指定された形式（CSV または Excel）に振り分け＆出力メソッド呼出し
	public void export(String format, HttpServletResponse response, boolean inline) throws IOException {
		List<BookListDto> books = bookListService.getBookList();

		if ("csv".equalsIgnoreCase(format)) {
			exportCsv(books, response);
		} else if ("excel".equalsIgnoreCase(format)) {
			exportExcel(books, response);
		} else if ("pdf".equalsIgnoreCase(format)) {
			exportPdf(books, response, inline); // ★ PDF追加
		}
	}

	// 書籍一覧を CSV ファイルとして出力する処理
	private void exportCsv(List<BookListDto> books, HttpServletResponse response) throws IOException {
		response.setContentType("text/csv; charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=bookLists.csv");

		// UTF-8だと文字化けするのでMS932（Shift_JIS互換）に変更(もう少し調べる）
		try (PrintWriter writer = new PrintWriter(
				new OutputStreamWriter(response.getOutputStream(), Charset.forName("MS932")))) {

			// ヘッダー行を全フィールドに合わせる
			writer.println("管理ID,タイトル,著者,ステータス,レビュー");

			// データ行を出力
			for (BookListDto book : books) {
				String reviewStr = (book.getReview() != null)
						? String.format("%.1f", book.getReview())
						: ""; // nullなら空欄にする

				writer.printf("%s,%s,%s,%s,%s%n",
						book.getManagementId(),
						book.getTitle(),
						book.getAuthor(),
						book.getStatus(),
						reviewStr);
			}

			// 「バッファに溜まったデータを全部出力先に流す」命令 closeにflushが含まれるため不要
			//writer.flush(); 

			//　try-with-resourcesで呼ばれるので不要
			//writer.close();
		}
	}

	// 書籍一覧を Excel ファイルとして出力する処理
	private void exportExcel(List<BookListDto> books, HttpServletResponse response) throws IOException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=bookLists.xlsx");

		// 新しい Excelファイル（.xlsx形式） をメモリ上に作成
		try (Workbook workbook = new XSSFWorkbook()) {

			// Excelファイルの中に 新しいシート を作成。タブ名("書籍一覧")
			Sheet sheet = workbook.createSheet("書籍一覧");

			// 列幅を固定
			sheet.setColumnWidth(0, 25 * 256);
			sheet.setColumnWidth(1, 40 * 256);
			sheet.setColumnWidth(2, 30 * 256);
			sheet.setColumnWidth(3, 10 * 256);
			sheet.setColumnWidth(4, 10 * 256);

			// ヘッダー部作成
			makeHeader(workbook, sheet);

			// ボディ部作成
			makeBody(workbook, sheet, books);

			// HttpServletResponse の出力ストリームに書き込む
			// HttpServletResponse の出力ストリームはコンテナ（Tomcatなど）がレスポンス終了時に閉じてくれるのでtry-with-resources不要
			workbook.write(response.getOutputStream());

			//　コンテナ（Tomcatなど）がレスポンス終了時に呼び出すcloseにflushが含まれるため不要
			//response.getOutputStream().flush();

			//　try-with-resourcesで呼ばれるので不要
			//workbook.close(); 

		}
	}

	//　ヘッダー部作成
	private void makeHeader(Workbook workbook, Sheet sheet) {

		// ヘッダー用スタイルを先に作成
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		//フォントを設定
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerStyle.setFont(headerFont);

		// ヘッダー行を作成
		Row header = sheet.createRow(0);

		// ヘッダー項目を設定
		String[] headers = { "管理ID", "タイトル", "著者", "ステータス", "レビュー" };
		for (int i = 0; i < headers.length; i++) {
			Cell cell = header.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

	}

	//　ボディ部作成
	private void makeBody(Workbook workbook, Sheet sheet, List<BookListDto> books) {

		//データ用スタイル
		CellStyle dataStyle = workbook.createCellStyle();
		dataStyle.setBorderTop(BorderStyle.THIN);
		dataStyle.setBorderBottom(BorderStyle.THIN);
		dataStyle.setBorderLeft(BorderStyle.THIN);
		dataStyle.setBorderRight(BorderStyle.THIN);

		// データ行
		int rowIdx = 1;
		for (BookListDto book : books) {
			Row row = sheet.createRow(rowIdx++);

			Cell cell0 = row.createCell(0);
			cell0.setCellValue(book.getManagementId());
			cell0.setCellStyle(dataStyle);

			Cell cell1 = row.createCell(1);
			cell1.setCellValue(book.getTitle());
			cell1.setCellStyle(dataStyle);

			Cell cell2 = row.createCell(2);
			cell2.setCellValue(book.getAuthor());
			cell2.setCellStyle(dataStyle);

			Cell cell3 = row.createCell(3);
			cell3.setCellValue(book.getStatus());
			cell3.setCellStyle(dataStyle);

			Cell cell4 = row.createCell(4);
			if (book.getReview() != null) {
				cell4.setCellValue(book.getReview());
			} else {
				cell4.setCellValue("");
			}
			cell4.setCellStyle(dataStyle);
		}

	}

	// 書籍一覧をPDFで出力
	private void exportPdf(List<BookListDto> books, HttpServletResponse response, boolean inline) throws IOException {
		// 新しい空のPDFドキュメントを作成（ノートの作成）
		try (PDDocument doc = new PDDocument()) {

			//　新しいページを作成（ルーズリーフ）
			PDPage page = new PDPage();

			//　PDFドキュメントにページを追加（ノートにルーズリーフを差し込む）
			doc.addPage(page);

			// ページの中身を読み込む（ルーズリーフに書き込み）
			try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

				// ページに書き込み
				cs.beginText();

				// フォント設定 日本語などの多言語に対応する Type 0 フォント（CIDフォント）を、PDFドキュメント doc に埋め込む
				PDType0Font font = PDType0Font.load(doc,
						new File("C:/Windows/Fonts/NotoSansJP-VF.ttf")); // Noto Sans JP

				// 文字サイズを 12 pt
				cs.setFont(font, 12);

				// 左余白 50、上方向に 750 の位置にカーソルを置くイメージです。この位置からテキストが描画されます。
				cs.newLineAtOffset(50, 750);

				// ヘッダー
				cs.showText("管理ID | タイトル | 著者 | ステータス | レビュー");
				cs.newLineAtOffset(0, -20);

				// データ行
				for (BookListDto book : books) {
					String line = book.getId() + " | "
							+ book.getTitle() + " | "
							+ book.getAuthor() + " | "
							+ book.getStatus() + " | "
							+ book.getReview();
					cs.showText(line);
					cs.newLineAtOffset(0, -20);
				}

				cs.endText();
			}

			response.setContentType("application/pdf");
			//throw new RuntimeException();

			// 表示の場合
			if (inline) {
				response.setHeader("Content-Disposition", "inline; filename=books.pdf");

				// 出力の場合
			} else {
				//　filename=　半角英数字のみファイル名に設定できる
				//response.setHeader("Content-Disposition", "attachment; filename=books.pdf");

				// ダウンロードさせたいファイル名
				String filename = "本.pdf";

				// ContentDisposition ビルダーで UTF-8 指定
				ContentDisposition disposition = ContentDisposition
						.attachment()
						.filename(filename, StandardCharsets.UTF_8)
						.build();

				// ヘッダーに設定
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());

			}

			try (OutputStream os = response.getOutputStream()) {
				doc.save(os);
			}
		}
	}

}
