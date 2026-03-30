import * as PROPERTY from "./module/const.js"; // heder,token
import { showRegistering, onlyShowSuccess, showError } from "./module/registerLoad.js";

// 共通処理関数
function setupModal(modalId, confirmButtonId, messageElementId, messageTemplate, apiPath, successMessage, errorMessage) {
	 const registerModal = new bootstrap.Modal(document.getElementById("registerModal"));
	 const modalMessage = document.getElementById("modalMessage");
	 const loadingSpinner = document.getElementById("loadingSpinner");
	 const closeModalBtn = document.getElementById("closeModalBtn");
	 const modal = document.getElementById(modalId);
	 const confirmButton = document.getElementById(confirmButtonId);

	  let currentId = null; // モーダルで選択された本のIDを保持。他の関数でIDwお使いたいからletにしておく
	
  // モーダルが開いたときの処理
  modal.addEventListener('show.bs.modal', event => {	
    const button = event.relatedTarget;
    const title = button.getAttribute('data-title');
    currentId = button.getAttribute('data-id'); // ここで選択した本のID保持しておく

    console.log("モーダルを開きました", title, currentId);

    const messageText = messageTemplate.replace("{title}", title);
    document.getElementById(messageElementId).textContent = messageText;
  });

  // confirmButtonを押したときの処理
    confirmButton.addEventListener("click",() => {
		
		// 登録中...を表示
		showRegistering(modalMessage, loadingSpinner, closeModalBtn, registerModal);
		
		// 確認のモーダルを閉じる
		const returnModalElement = document.getElementById(modalId);
		const returnModalInstance = bootstrap.Modal.getOrCreateInstance(returnModalElement);
		returnModalInstance.hide();

		
		setTimeout(() => {
		// fetch(url, { method, headers, body }) の形
		fetch(`${apiPath}/${currentId}`, {
		  method: "POST",
		  headers: {
		    "Content-Type": "application/json",
		    [PROPERTY.header]: PROPERTY.token //　認証トークン
		  }
		})
        .then(res => {
		   if (res.ok) {
			const messages = successMessage;
			 onlyShowSuccess(modalMessage, loadingSpinner, closeModalBtn, messages);
			// location.reload();
			 } else {
			 const messages = errorMessage; // 失敗時（サーバー側の処理結果が「失敗」）
			 showError(modalMessage, loadingSpinner, closeModalBtn, messages);
		   }
        })
        .catch(err => { //サーバーからレスポンスが返ってこない
          console.error(err);
          alert("通信エラーが発生しました");
        });
		}, 2000);
    });
}	


// 返却モーダル設定
setupModal(
  "returnModal",           // モーダルID
  "confirmReturnButton",      // 確定ボタンID
  "returnMessage", // 返却用の要素ID
  "『{title}』を返却しますか？", // メッセージテンプレート
  `${CONTEXT_PATH}/api/book/return`,    // APIパス
  "返却が完了しました",     // 成功メッセージ
  "返却に失敗しました"      // 失敗メッセージ
);

// キャンセルモーダル設定
setupModal(
  "cancelModal",
  "confirmCancelButton",
  "cancelMessage", 
  "『{title}』の予約をキャンセルしますか？",
  `${CONTEXT_PATH}/api/book/reserve/cancel`,
  "予約をキャンセルしました", 
  "キャンセルに失敗しました"  
);
