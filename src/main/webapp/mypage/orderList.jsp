<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>  
<%@ include file="/header.jsp" %>  
<%@ include file="/mypage/sub_img.html"%> 
<%@ include file="/mypage/sub_menu.jsp" %>       
  <article>
      <h2> Order List </h2>
      <form name="formm" method="post">
        <table id="cartList">
       <tr>
        <th>상품명</th> <th>수 량</th><th>가 격</th> <th>주문일</th> <th> 진행 상태 </th>    
       </tr>
       <c:forEach items="${orderList}"  var="orderVO">
       <tr>      
        <td>
            <a href="${contextPath}/action/product/detail?pseq=${orderVO.pseq}">
              <h3> ${orderVO.pname} </h3>              
          </a>    
        </td>
        <td> ${orderVO.quantity} </td>
        <td> <fmt:formatNumber value="${orderVO.price2*orderVO.quantity}" type="currency"/> </td>      
        <td> <fmt:formatDate value="${orderVO.indate}" type="date"/></td>
        <c:choose>
        <c:when test="${orderVO.result eq '1' }">
        <td> 처리 진행 중 </td>
        </c:when>
        <c:otherwise>
        <td> 처리 완료 </td>
        </c:otherwise>
        </c:choose>
        
       </tr>
       </c:forEach>
       <tr>
         <th colspan="2"> 총 액 </th>
         <th colspan="2"> <fmt:formatNumber value="${totalPrice}" type="currency"/><br>     </th>             
       </tr> 
      </table>   
          
      <div class="clear"></div>
      <div id="buttons" style="float: right">
       <input type="button"    value="쇼핑 계속하기"  class="cancel"  onclick="location.href='${contextPath}/action/index'">     
      </div>
    </form>  
  </article>
<%@ include file="/footer.jsp" %>