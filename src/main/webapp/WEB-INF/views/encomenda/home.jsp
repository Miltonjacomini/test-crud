<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/template" prefix="template" %>
<template:admin>
<jsp:body>
     <h2 class="basic-title">Lista de encomendas</h2>
       <div class="container">
		<div class="row">
			<div class="col-lg-12">
				<c:if test="${empty encomendas}" >
					<h3>Nenhuma encomenda realizada!</h3>
				</c:if>
				<c:forEach items='${encomendas}' var='encomenda'>
					<table class="table table-striped">
						<tr>
							<th>Data Envio</th>
							<th>Pedido</th>
							<th>Status</th>
							<th>Total pedido R$</th>
							<th>
								<c:if test="${encomenda.status == 'ENVIADO'}">
									<td>
										<a href="<c:url value='/encomenda/confirma'/>/${encomenda.id}">
			          						<span class="btn btn-default btn-sweetmanu">Confirmar</span>
			          					</a>
			          				</td>
		          				</c:if>
		          				<c:if test="${encomenda.status == 'CONFIRMADO'}">
			          				<td>
										<a href="<c:url value='/encomenda/emPreparo'/>/${encomenda.id}">
			          						<span class="btn btn-default btn-sweetmanu">Em preparo</span>
			          					</a>
			          				</td>
		          				</c:if>
		          				<c:if test="${encomenda.status == 'EM_PREPARO'}">
			          				<td>
										<a href="<c:url value='/encomenda/finalizado'/>/${encomenda.id}">
			          						<span class="btn btn-default btn-sweetmanu">Finalizado</span>
			          					</a>
			          				</td>
		          				</c:if>
		          				<c:if test="${encomenda.status == 'FINALIZADO'}">
			          				<td>
										<a href="<c:url value='/encomenda/entregue'/>/${encomenda.id}">
			          						<span class="btn btn-default btn-sweetmanu">Entregue</span>
			          					</a>
			          				</td>
		          				</c:if>
							</th>
						</tr>
						<tr>	
							<td>
								<template:localDate pattern="dd/MM/yyyy" date="${pedido.dtCriacao}"/>
							</td>
							<td>${encomenda.id}</td>
							<td>${encomenda.status}</td>
							<td>${encomenda.getValorTotal(encomenda.produtos)}</td>
						</tr>
						<tr>
							<table class="table table-condensed">
								<tr>
									<th>Produto</th>
									<th>Quantidade</th>
									<th>Valor</th>
								</tr>
								<c:forEach items='${encomenda.produtos}' var='produto'>
									<tr>	
										<td>${produto.nome}</td>
										<td>${encomenda.getQuantidadeProduto(produto)}</td>
										<td>${produto.valor}</td>
									</tr>
								</c:forEach>
							</table>
						</tr>
					</table>
					</br>
				</c:forEach>
			</div>
		</div>
	</div>
</jsp:body>
</template:admin>
