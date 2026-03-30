import * as PROPERTY from "./module/const.js"; // heder,token
import { showRegistering, showSuccess, showError } from "./module/registerLoad.js";
import { saveAs } from "./module/file-saver-es/FileSaver.js";

document.getElementById("exportForm").addEventListener("submit", function(e) {
  e.preventDefault(); // ブラウザの通常送信を止める
  const format = document.getElementById("format").value; // selectやinputから取得
  exportBooks(format); // ←ここで呼ぶ
});

document.querySelector(`a[href='${CONTEXT_PATH}/api/book-list/view']`).addEventListener("click", function(e) {
  e.preventDefault(); // 通常のリンク遷移を止める
  viewBooks(); // ←ここで呼ぶ
});

//通信結果モーダル用
const registerModal = new bootstrap.Modal(document.getElementById("registerModal"));
const modalMessage = document.getElementById("modalMessage");
const loadingSpinner = document.getElementById("loadingSpinner");
const closeModalBtn = document.getElementById("closeModalBtn");

// 登録中...を表示
//showRegistering(modalMessage, loadingSpinner, closeModalBtn, registerModal);

//ファイル出力
function exportBooks(format) {
  showRegistering(modalMessage, loadingSpinner, closeModalBtn, registerModal);

  fetch(`${CONTEXT_PATH}/api/book-list/export?format=${format}`, {
    method: "GET",
    headers: {
      [PROPERTY.header]: PROPERTY.token
    }
  })
    .then(response => {
      if (!response.ok) {
        // サーバーが sendError で返したメッセージを取得
        return response.text().then(text => { throw new Error(text); });
      }
      return response.blob();
    })
    .then(blob => {
      const filename = "books." + (format === "excel" ? "xlsx" : format);
	  saveAs(blob, filename); 
      showSuccess(modalMessage, loadingSpinner, closeModalBtn, `${filename} をダウンロードしました`);
    })
    .catch(error => {
      // error.message に sendError のメッセージが入る
      showError(modalMessage, loadingSpinner, closeModalBtn, error.message);
    });
}

//PDF画面表示
function viewBooks() {
  showRegistering(modalMessage, loadingSpinner, closeModalBtn, registerModal);

  fetch(`${CONTEXT_PATH}/api/book-list/view`, {
    method: "GET",
    headers: {
      [PROPERTY.header]: PROPERTY.token
    }
  })
    .then(response => {
      if (!response.ok) {
        return response.text().then(text => { throw new Error(text); });
      }
      return response.blob();
    })
    .then(blob => {
      const url = URL.createObjectURL(blob);
      window.open(url, "_blank");
      showSuccess(modalMessage, loadingSpinner, closeModalBtn, "PDFを表示しました");
    })
    .catch(error => {
	  // サーバーからの詳細メッセージをログに残す
	  console.error(error);
      showError(modalMessage, loadingSpinner, closeModalBtn,"PDFの出力に失敗しました。再度お試しください。");
    });
}
