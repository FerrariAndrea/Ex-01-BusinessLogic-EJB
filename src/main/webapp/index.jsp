<%@ page session ="true"%>
<%@ page import="java.util.*" %>
<%@ page import="it.distributedsystems.model.dao.*" %>
<%@ page import="org.hibernate.Hibernate" %>
<%@ page import="it.distributedsystems.model.ejb.CartBean" %>
<%@ page import="javax.ejb.EJB" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="it.distributedsystems.model.ejb.Cart" %>

<%!


	String printTableRow(Product product, String url,boolean deletable) {
		StringBuffer html = new StringBuffer();
		html
				.append("<tr>")
				.append("<td>")
				.append(product.getName())
				.append("</td>")

				.append("<td>")
				.append(product.getProductNumber())
				.append("</td>")

				.append("<td>")
				.append( (product.getProducer() == null) ? "n.d." : product.getProducer().getName() )
				.append("</td>")

				.append("<td>");
				if(deletable){
					html.append( "<form><input type=\"hidden\" name=\"operation\" value=\"removeFromCart\"/><input type=\"hidden\" name=\"dellID\" value=\""+product.getId()+"\"/><input type=\"submit\" name=\"submit\" value=\"DELL\"/></form>" );
				}else{
					html.append( product.getId() );
				}
				html.append("</td>").append("</tr>");

		return html.toString();
	}
	String printSelectorTableRow(Product product, String url) {
		StringBuffer html = new StringBuffer();
		String producer ="";
		if(product.getProducer()!=null){
			producer= " - producer: "+product.getProducer().getName();
		}
		html.append("<option maxQ="+product.getProductNumber()+" value="+product.getId()+">"+product.getName()+producer+"</option>");
		return html.toString();
	}

	String printTableRows(List products, String url,boolean deletable) {
		StringBuffer html = new StringBuffer();
		Iterator iterator = products.iterator();
		while ( iterator.hasNext() ) {
			html.append( printTableRow( (Product) iterator.next(), url,deletable ) );
		}
		return html.toString();
	}

	String printSelectorTableRows(List products, String url) {
		StringBuffer html = new StringBuffer();
		Iterator iterator = products.iterator();
		while ( iterator.hasNext() ) {
			html.append( printSelectorTableRow( (Product) iterator.next(), url ) );
		}
		return html.toString();
	}
%>

<html>

	<head>
		<title>HOMEPAGE DISTRIBUTED SYSTEM EJB</title>
	
		<meta http-equiv="Pragma" content="no-cache"/>
		<meta http-equiv="Expires" content="Mon, 01 Jan 1996 23:59:59 GMT"/>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<meta name="Author" content="you">

		<link rel="StyleSheet" href="styles/default.css" type="text/css" media="all" />
		<script src="js/myjs.js"></script>
	</head>
	
	<body>

	<%


		// can't use builtin object 'application' while in a declaration!
		// must be in a scriptlet or expression!

		//TRAMITE dep.inj ---------> non va :(
	/*
	@EJB
	Cart cart;
	//TRAMITE LOOKUP
	*/
	Cart cart;
	if(session.getAttribute("cart") ==null){
        InitialContext ic = new InitialContext();
        cart = (Cart) ic.lookup("java:global/distributed-systems-demo/distributed-systems-demo.war/carrello");
        session.setAttribute("cart",cart);
    }else{
        cart= (Cart)session.getAttribute("cart");
    }
	/*
	public void init(){
		try{
			InitialContext ic = new InitialContext();
			//java:global/distributed-systems-demo/distributed-systems-demo.war/CartBean!it.distributedsystems.model.ejb.Cart
			//java:app/distributed-systems-demo.war/CartBean!it.distributedsystems.model.ejb.Cart
			//java:global/distributed-systems-demo/distributed-systems-demo.war/carrello
			cart = (Cart) ic.lookup("java:global/distributed-systems-demo/distributed-systems-demo.war/carrello");
		}catch (Exception e){
			System.out.println("------->FATAL ERROR ON LOOKUP FOR CART: "+ e.toString());
		}

	}
	*/
		DAOFactory daoFactory = DAOFactory.getDAOFactory( application.getInitParameter("dao") );
		CustomerDAO customerDAO = daoFactory.getCustomerDAO();
		PurchaseDAO purchaseDAO = daoFactory.getPurchaseDAO();
		ProductDAO productDAO = daoFactory.getProductDAO();
		ProducerDAO producerDAO = daoFactory.getProducerDAO();


		String operation = request.getParameter("operation");
		if ( operation != null && operation.equals("insertCustomer") ) {
			Customer customer = new Customer();
			customer.setName( request.getParameter("name") );
			int id = customerDAO.insertCustomer( customer );
			out.println("<!-- inserted customer '" + customer.getName() + "', with id = '" + id + "' -->");
		}
		else if ( operation != null && operation.equals("insertProducer") ) {
			Producer producer = new Producer();
			producer.setName( request.getParameter("name") );
			int id = producerDAO.insertProducer( producer );
			out.println("<!-- inserted producer '" + producer.getName() + "', with id = '" + id + "' -->");
		}
		else if ( operation != null && operation.equals("insertProduct") ) {

			Product product = new Product();
			product.setName( request.getParameter("name") );
			String name = product.getName();
			product.setProductNumber(Integer.parseInt(request.getParameter("number")));
			if(request.getParameter("producer")!=null){
				Producer producer = producerDAO.findProducerById(Integer.parseInt(request.getParameter("producer")));
				product.setProducer(producer);
			}
			int id = productDAO.insertProduct(product);
			System.out.println("productDAO.insertProduct--->" + name);
			out.println("<!-- inserted product '" +name+ "' with id = '" + id + "' -->");
		}else  if( operation != null && operation.equals("addToCart") ){	//Da aggiungere la possibilità di fare un ordine in sessione e di finalizzarla per creare un purchase.
			cart.addToCart(productDAO.findProductById(Integer.parseInt(request.getParameter("productToCart"))));
		}else  if( operation != null && operation.equals("removeFromCart") ){
			cart.removeFromCart(productDAO.findProductById(Integer.parseInt(request.getParameter("dellID"))));
		}else  if( operation != null && operation.equals("confirm") ){

		}

	%>


	<h1>Customer Manager</h1>

	<div>
		<p>Add Customer:</p>
		<form>
			Name: <input type="text" name="name"/><br/>
			<input type="hidden" name="operation" value="insertCustomer"/>
			<input type="submit" name="submit" value="submit"/>
		</form>
	</div>

	<div>
		<p>Add Producer:</p>
		<form>
			Name: <input type="text" name="name"/><br/>
			<input type="hidden" name="operation" value="insertProducer"/>
			<input type="submit" name="submit" value="submit"/>
		</form>
	</div>

	<div>
		<p>Add Product:</p>
		<form>
			Name: <input type="text" name="name"/><br/>
			Product Number: <input type="text" name="number"/><br/>
			<input type="hidden" name="operation" value="insertProduct"/>
			<input type="submit" name="submit" value="submit"/>
		</form>
	</div>
	<%
		List producers = producerDAO.getAllProducers();
		if ( producers.size() > 0 ) {
	%>
	<div>
		<p>Add Product:</p>
		<form>
			Name: <input type="text" name="name"/><br/>
			Product Number: <input type="text" name="number"/><br/>
			Producers: <select name="producer">
			<%
				Iterator iterator = producers.iterator();
				while ( iterator.hasNext() ) {
					Producer producer = (Producer) iterator.next();
			%>
			<option value="<%= producer.getId() %>"><%= producer.getName()%></option>
			<%
				}// end while
			%>
			</select>
			<input type="hidden" name="operation" value="insertProduct"/>
			<input type="submit" name="submit" value="submit"/>
		</form>
	</div>
	<%
	}// end if
	else {
	%>
	<div>
		<p>At least one Producer must be present to add a new Product.</p>
	</div>
	<%
		} // end else
		List<Product> allProduct =productDAO.getAllProducts();
	%>
	<div>
		<p>Products currently in the database:</p>
		<table>
			<tr><th>Name</th><th>ProductNumber</th><th>Publisher</th><th>ID</th></tr>
			<%= printTableRows( allProduct, request.getContextPath() ,false) %>
		</table>
	</div>

	<div>
		<a href="<%= request.getContextPath() %>">Ricarica lo stato iniziale di questa pagina</a>
	</div>
	<hr><br>
	<div>
		<form>
			<label>Seleziona il prodotto:</label>
			<select onchange="selection(this)" name="productToCart">
			<%= printSelectorTableRows( allProduct, request.getContextPath() ) %>
			</select><br>
			<!--<label>Seleziona la quantit&agrave;</label><input type="number"  max="0" min="0" id="quantity" name="quantity" value=""/>-->
			<input type="hidden" name="operation" value="addToCart"/>
			<input type="submit" name="submit" value="submit"/>
		</form>
			<div>
			<label>Carrello corrente</label>
				<table>
					<tr><th>Name</th><th>ProductNumber</th><th>Publisher</th><th>Remove</th></tr>
					<%= printTableRows( cart.getPorductsInCart(), request.getContextPath() ,true) %>
				</table>
		</div>
	</div>
	</body>

</html>