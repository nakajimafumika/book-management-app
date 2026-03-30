import * as PROPERTY from "./module/const.js"; // heder,token
import { showRegistering, showSuccess, showError } from "./module/registerLoad.js";

// 入力欄のDOM要素
const titleInput = document.getElementById("title");
const authorInput = document.getElementById("author");
const isbnInput = document.getElementById("isbnCode");
const urlInput = document.getElementById("url");
const memoInput = document.getElementById("memo");

//イベント発火 検索ボタンクリック
document.getElementById("searchButton").addEventListener("click", searchBook);

//イベント発火 Enterキー押下
isbnInput.addEventListener("keydown", (event) => {
  if (event.key === "Enter") {
    searchBook();
  }
});

// ISBNコードのバリデーション
function isValidIsbn(isbn) {
  // 数字のみ（XはISBN10の末尾で使われることがある）
  const regex = /^(?:\d{9}[\dX]|\d{13})$/;
  return regex.test(isbn);
}

// 本の情報をISBNコードをもとにAPIで取得
function searchBook() {
	const isbn =  isbnInput.value.trim().replace(/-/g, "");
	const bookImage = document.getElementById("bookImage");
	
	// ISBNが不正なら処理を止める
	if (!isValidIsbn(isbn)) {
	  alert("ISBNコードが不正です");
	  return;
	}
    // 書影画像URLをセット
    bookImage.src = `https://ndlsearch.ndl.go.jp/thumbnail/${isbn}.jpg`;

    // 画像が存在しない場合は noimage.png に切り替え
    bookImage.onerror = function() {
       this.src = "/images/noimage.png";
    };

    // 検索成功時に表示（初期は d-none にしている場合）
    bookImage.classList.remove("d-none");

    const apiUrl = `https://ndlsearch.ndl.go.jp/api/opensearch?isbn=${isbn}&cnt=1`;
  
  // 書籍情報取得
  fetch(apiUrl)
    .then(response => response.text())
    .then(xmlText => {
      const parser = new DOMParser();
      const xmlDoc = parser.parseFromString(xmlText, "application/xml"); //xmlText を XML として解釈し、xmlDoc に「DOMオブジェクト」として格納
	  
	  
      const item = xmlDoc.getElementsByTagName("item")[0];
	  
	  if (!item) {
	  	      alert("書誌情報が見つかりませんでした");
	  	      return;
	  	    }
			
      const titleFromApi = item.getElementsByTagName("title")[0]?.textContent || "不明";
	  const authorFromApi = Array.from(item.getElementsByTagName("dc:creator"))
	     .map(el => (el.textContent || "不明").replace(",", "").trim())
	     .join(",");
		 
	   // フォームに反映
	   titleInput.value = titleFromApi;
	   authorInput.value = authorFromApi;
    })
    .catch(err => {
      console.error("エラー:", err);
      alert("書誌情報の取得に失敗しました");
    });
}


document.getElementById("registerButton").addEventListener("click", () => {
	
  // 送信データ
  const bookrequestInfo = {
	isbnCode: isbnInput.value,
    title: titleInput.value,
    author: authorInput.value,
	url: urlInput.value,
	memo: memoInput.value
  };
  
  const registerModal = new bootstrap.Modal(document.getElementById("registerModal"));
  const modalMessage = document.getElementById("modalMessage");
  const loadingSpinner = document.getElementById("loadingSpinner");
  const closeModalBtn = document.getElementById("closeModalBtn");
  
  // 登録中...を表示
  showRegistering(modalMessage, loadingSpinner, closeModalBtn, registerModal);

  // 登録処理（ローディングのために2秒経ってから fetch を実行する）
  setTimeout(() => {
	fetch(`${CONTEXT_PATH}/api/book-request`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      [PROPERTY.header]: PROPERTY.token
    },
    body: JSON.stringify(bookrequestInfo)
  })
  .then(response => {
    if (!response.ok) {
      // エラーレスポンスを JSON としてパース
	  return response.json()
	  .then(errorData => {	
		  return Promise.reject(errorData);
	  }
  );
    }
    return; // 正常時のレスポンスを返す
  })
    .then(() => {
	 const messages = "登録が完了しました！ 3秒後に画面をリセットします...";
      console.log("登録成功");
      showSuccess(modalMessage, loadingSpinner, closeModalBtn, messages);
    })
    .catch(error => {
		// メッセージを改行で見やすくする
	const messages = Object.values(error).join("<br>");
      console.error("登録失敗:", error);
	  showError(modalMessage, loadingSpinner, closeModalBtn, messages);
    });
	
	}, 2000);
});


