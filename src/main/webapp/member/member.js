function go_save() {
  if (document.formm.id.value == "") {
    alert("아이디는 입력해주세요.");
    document.formm.id.focus();
  } else if (document.formm.id.value != document.formm.reid.value) {
    alert("아이디가 일치하지 않습니다.");
    document.formm.id.focus();
  } else if (document.formm.pwd.value == "") {
    alert("비밀번호를 입력해주세요.");
    document.formm.pwd.focus();
  } else if ((document.formm.pwd.value != document.formm.pwdCheck.value)) {
    alert("비밀번호가 일치하지 않습니다.");
    document.formm.pwd.focus();
  } else if (document.formm.name.value == "") {
    alert("이름을 입력해주세요.");
    document.formm.name.focus();
  } else if (document.formm.email.value == "") {
    alert("이메일을 입력해주세요.");
    document.formm.email.focus();
  } else {
    document.formm.action = "/shopping_complete/action/member/join";
    document.formm.submit();
  }
}

function idcheck() {
  if (document.formm.id.value == "") {
    alert('아이디를 입력해주세요.');
    document.formm.id.focus();
    return;
  }
  var url = "/shopping_complete/action/member/idCheck?id=" 
+ document.formm.id.value;
  window.open( url, "_blank_1",
"toolbar=no, menubar=no, scrollbars=yes, resizable=no, width=330, height=200");
}

function post_zip() {
  var url = "/shopping_complete/action/member/findZipNum";
  window.open( url, "_blank_1",
"toolbar=no, menubar=no, scrollbars=yes, resizable=no, width=550, height=300, top=300, left=300, ");
}

function go_next() {
  if (document.formm.okon1[0].checked == true) {
    document.formm.action = "/shopping_complete/action/member/joinForm";
    document.formm.submit();
  } else if (document.formm.okon1[1].checked == true) {
    alert("약관 동의는 필수입니다.");
  }
}