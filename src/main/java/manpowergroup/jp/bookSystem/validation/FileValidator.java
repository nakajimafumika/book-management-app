package manpowergroup.jp.bookSystem.validation;

import org.springframework.web.multipart.MultipartFile;

public class FileValidator {

	// 許可する拡張子
	private static final String[] ALLOWED_EXTENSIONS = { ".png", ".jpg", ".jpeg" };

	// 許可するMIMEタイプ
	private static final String[] ALLOWED_MIME_TYPES = { "image/png", "image/jpeg" };

	public static void validateImageFile(MultipartFile file) {
		String originalName = file.getOriginalFilename();
		String contentType = file.getContentType();

		// --- 拡張子チェック ---
		boolean extensionAllowed = false;// 初期状態では「許可されていない」
		if (originalName != null) {
			String lowerName = originalName.toLowerCase(); // 小文字に変換（大文字小文字を区別しないため）
			for (String ext : ALLOWED_EXTENSIONS) { //ループの1回ごとに、変数extにALLOWED_EXTENSIONS の要素が順番に代入される。
				if (lowerName.endsWith(ext)) { // ファイル名がその拡張子で終わっているか判定
					extensionAllowed = true; // 許可された拡張子なら true にする
					break;
				}
			}
		}
		if (!extensionAllowed) {
			throw new IllegalArgumentException("拡張子が不正です。PNG/JPG/JPEGのみアップロード可能です");
		}

		// --- MIMEタイプチェック ---
		boolean mimeAllowed = false;
		if (contentType != null) {
			for (String mime : ALLOWED_MIME_TYPES) {
				if (mime.equalsIgnoreCase(contentType)) {
					mimeAllowed = true;
					break;
				}
			}
		}
		if (!mimeAllowed) {
			throw new IllegalArgumentException("MIMEタイプが不正です。PNG/JPG/JPEGのみアップロード可能です");
		}
	}
}
