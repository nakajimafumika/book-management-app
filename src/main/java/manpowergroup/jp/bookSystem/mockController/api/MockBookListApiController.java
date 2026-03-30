package manpowergroup.jp.bookSystem.mockController.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("mock")
@RequestMapping("/api")
public class MockBookListApiController {

	@GetMapping("/book-list")
	public ResponseEntity<?> getBookList() {
		try {
			// resources/mock/items.json を読み込む
			ClassPathResource resource = new ClassPathResource("mock/items.json");//クラスパス上のファイルを読み込む
			InputStream inputStream = resource.getInputStream();//バイトストリームとして取得
			String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);//ファイル全体を byte[] に読み込む→バイト列を UTF-8 文字列に変換

			// JSON文字列をそのまま返す
			return ResponseEntity.ok()//HTTPステータスコード 200（成功）を指定
					.contentType(MediaType.APPLICATION_JSON)//レスポンスの Content-Type を application/json に設定
					.body(json);//実際に返すデータ（ここではJSON文字列）をレスポンスの本文にセット

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("データの読み込みに失敗しました");
		}
	}

}
