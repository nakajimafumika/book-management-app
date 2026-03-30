// registerLoad.js

// 登録中表示
export function showRegistering(modalMessage, loadingSpinner, closeModalBtn, registerModal) {
  modalMessage.style.color = "";
  modalMessage.style.fontWeight = "";
  registerModal.show();
  modalMessage.textContent = "登録中...";
  loadingSpinner.style.display = "inline-block";
  closeModalBtn.style.display = "none";
}

// 登録成功表示 → 3秒後に画面リセット
export function showSuccess(modalMessage, loadingSpinner, closeModalBtn, messages) {
  modalMessage.style.color = "";
  modalMessage.style.fontWeight = "";
  modalMessage.textContent = messages;
  loadingSpinner.style.display = "none";
  closeModalBtn.style.display = "none"; // 完了時は閉じるボタンを隠す

  // 3秒後に画面リロード
  setTimeout(() => {
    window.location.reload();
  }, 3000);
}

// 登録成功表示のみ
export function onlyShowSuccess(modalMessage, loadingSpinner, closeModalBtn, messages) {
  modalMessage.style.color = "";
  modalMessage.style.fontWeight = "";
  modalMessage.textContent = messages;
  loadingSpinner.style.display = "none";
  closeModalBtn.style.display = "none"; // 完了時は閉じるボタンを隠す
}



// エラー表示（リセットはしない）
export function showError(modalMessage, loadingSpinner, closeModalBtn, messages) {
  modalMessage.innerHTML = messages;
  modalMessage.style.color = "red";
  modalMessage.style.fontWeight = "bold";
  loadingSpinner.style.display = "none";
  closeModalBtn.style.display = "inline-block";
}

