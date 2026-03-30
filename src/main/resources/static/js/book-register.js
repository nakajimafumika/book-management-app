import dayjs from "./module/dayjs/index.js"; 
import * as PROPERTY from "./module/const.js"; // heder,token
import { showRegistering, showSuccess, showError } from "./module/registerLoad.js";

// 入力欄のDOM要素
const titleInput = document.getElementById("title");
const authorInput = document.getElementById("author");
const publisherInput = document.getElementById("publisher");
const publicationDateInput = document.getElementById("publicationDate");
const managementIdInput = document.getElementById("managementId");
const isbnInput = document.getElementById("isbnCode")
const uploadBookImageInput = document.getElementById("uploadBookImage");


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

function searchBook() {
  const isbn = isbnInput.value.trim().replace(/-/g, "");
  const bookImage = document.getElementById("bookImage");
  const uploadSection = document.getElementById("uploadSection");

  if (!isValidIsbn(isbn)) {
    alert("ISBNコードが不正です");
    return;
  }

  // onerror を先に設定
  bookImage.onerror = () => {
    bookImage.src = `${CONTEXT_PATH}/images/noimage.png`
  }
  
  // 書影画像URLをセット
  bookImage.src = `https://ndlsearch.ndl.go.jp/thumbnail/${isbn}.jpg`;

  // 表示
  bookImage.classList.remove("d-none");
  uploadSection.classList.remove("d-none");

// ファイル選択時にプレビュー
document.getElementById("uploadBookImage").addEventListener("change", (event)=>{
  console.log("changeイベント発火")
  const file = event.target.files[0];
  const bookImage = document.getElementById("bookImage");

  if (file) {
    const reader = new FileReader();
    reader.onload = e => {
		console.log("onload発火");
      bookImage.src = e.target.result;
	  uploadSection.classList.remove("d-none");
    };
    reader.readAsDataURL(file);
  }
});

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
	  const publisherFromApi = item.getElementsByTagName("dc:publisher")[0]?.textContent.trim() || "不明";
	  const issuedRaw = item.getElementsByTagName("dcterms:issued")[0]?.textContent;
	  const publicationDateFromApi = issuedRaw ? dayjs(`${issuedRaw}.01`, "YYYY.M.D").format("YYYY/MM") : "不明";

	   // フォームに反映
	   titleInput.value = titleFromApi;
	   authorInput.value = authorFromApi;
	   publisherInput.value = publisherFromApi;
	   publicationDateInput.value = publicationDateFromApi;
    })
    .catch(err => {
      console.error("エラー:", err);
      alert("書誌情報の取得に失敗しました");
    });
}

//登録処理イベント発火
document.getElementById("formRegisterButton").addEventListener("click", () => {
	
	// FormDataを作成
	  const formData = new FormData();

	// 変数を利用して値を追加
	 formData.append("isbnCode", isbnInput.value);
	 formData.append("managementId", managementIdInput.value);
	 formData.append("title", titleInput.value);
	 formData.append("author", authorInput.value);
	 formData.append("publisher", publisherInput.value);
	 formData.append("publicationDate", publicationDateInput.value);
	 
	 // ファイルが選択されていれば追加
	 const uploadBookImage = uploadBookImageInput.files[0];
	 if (uploadBookImage) {
	   formData.append("uploadBookImage", uploadBookImage);
	 }
	
  // 登録中モーダルに必要な変数
  const registerModal = new bootstrap.Modal(document.getElementById("registerModal"));
  const modalMessage = document.getElementById("modalMessage");
  const loadingSpinner = document.getElementById("loadingSpinner");
  const closeModalBtn = document.getElementById("closeModalBtn");
  
  // 登録中...を表示
  showRegistering(modalMessage, loadingSpinner, closeModalBtn, registerModal);

  // 登録処理（ローディングのために2秒経ってから fetch を実行する）
  setTimeout(() => {
    return fetch(`${CONTEXT_PATH}/api/book-register`, {
      method: "POST",
      headers: {
        [PROPERTY.header]: PROPERTY.token
		// Content-Type は指定しない（ブラウザが自動で multipart/form-data を付ける）
      },
      body: formData
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
  
  
  //ファイルから書籍情報を登録する処理
  document.getElementById("fileRegisterButton").addEventListener("click", () => {
    const fileInput = document.getElementById("bookFile");
    const file = fileInput.files[0];

    if (!file) {
      alert("ファイルを選択してください");
      return;
    }

    // FormDataを使ってファイルを送信
    const formData = new FormData();
    formData.append("file", file);

    // ローディング演出のために2秒待機
    setTimeout(() => {
      fetch(`${CONTEXT_PATH}/api/book-register-file`, {
        method: "POST",
        headers: {
          [PROPERTY.header]: PROPERTY.token 
        },
        body: formData
      })
      .then(response => {
		console.log("status:", response.status); 
		console.log("ok:", response.ok);
		
        if (!response.ok) {
          throw new Error("アップロードに失敗しました");
        }
        return response.json();
      })
      .then(data => {
        console.log("ファイル登録成功:", data);
        alert("ファイルから書籍登録が完了しました！");
      })
      .catch(error => {
        console.error("ファイル登録失敗:", error);
        alert("ファイル登録に失敗しました");
      });
    }, 2000);
  });


