/**
 * ログインフォームのバリデーション処理
 * @author nakajima
 */

document.querySelector("form").addEventListener("submit", function(e) {
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value.trim();
  const errorDiv = document.getElementById("error_message_label");

  let message = "";

  if (!email && !password) {
    message = "メールアドレスとパスワードを入力してください";
  } else if (!email) {
    message = "メールアドレスを入力してください";
  } else if (!password) {
    message = "パスワードを入力してください";
  }

  if (message) {
    errorDiv.textContent = message;
    errorDiv.style.display = "block";
    e.preventDefault();
  } else {
    errorDiv.style.display = "none"; // エラーがない場合は非表示に
  }
});
