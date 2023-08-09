function go_cart() {
  if (document.formm.quantity.value == "") {
    alert("수량을 입력하여 주세요.");
    document.formm.quantity.focus();
  } else {
    document.formm.action = "/shopping_complete/action/cart/insert";
    document.formm.submit();
  }
}

function go_cart_delete() {
	  var count = 0;

	  // 단일 체크박스인 경우 직접 checked 속성으로 확인합니다.
	  if (document.formm.cesq.length == undefined) {
		  if (document.formm.cseq.checked == true) {
		    count++;
		  }
	  }
	  
	  console.log('count' + count);

	  // 다중 체크박스인 경우 배열로 다루어 체크 여부를 확인합니다.
	  for (var i = 0; i < document.formm.cseq.length; i++) {
	    if (document.formm.cseq[i].checked == true) {
	      count++;
	    }
	  }

	  if (count == 0) {
	    alert("삭제할 항목을 선택해 주세요.");
	  } else {
	    document.formm.action = "/shopping_complete/action/cart/delete";
	    document.formm.submit();
	  }
	}


function go_order_insert() {
  document.formm.action = "/shopping_complete/action/order/insert";
  document.formm.submit();
}

function go_order_delete() {
  var count = 0;

  if (document.formm.oseq.length == undefined) {
    if (document.formm.oseq.checked == true) {
      count++;
    }
  }

  for ( var i = 0; i < document.formm.oseq.length; i++) {
    if (document.formm.oseq[i].checked == true) {
      count++;
    }
  }
  if (count == 0) {
    alert("삭제할 항목을 선택해 주세요.");

  } else {
    document.formm.action = "/shopping_complete/action/order/delete";
    document.formm.submit();
  }
}

function go_order() {
  document.formm.action = "/shopping_complete/action/member/mypage";
  document.formm.submit();
}