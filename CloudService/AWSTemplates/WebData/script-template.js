

function getParameter( name, url ) {
    if (!url) url = location.href;
    name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
    var regexS = "[\\?&]"+name+"=([^&#]*)";
    var regex = new RegExp( regexS );
    var results = regex.exec( url );
    return results == null ? null : results[1];
}

async function getProductUrl( id ){
	 
    const url = "https://{api-id}.execute-api.ap-southeast-1.amazonaws.com/verify/product/" + id;
    let response = await fetch(url);
    return await response.json();
}

function setProductLink() {
	var mainContentElement = document.getElementById("main-content");
    var linkElement = document.getElementById("product-link");
    //linkElement.style.visibility = "hidden";
    var pProdID = getParameter("p", null);
    var pRef = getParameter("ref", null);
    if(pProdID != null) {
		if(pRef == null) {
			mainContentElement.style.visibility = "visible";
		}
        getProductUrl(pProdID.trim()).then((json) => {
            try {
                let productWebpage = json.Item.ProductWebpage.S;
                if ((productWebpage != null) && (productWebpage != "")) {                

                    // Auto redirect if referred by app
                    if((pRef != null) && (pRef.toLowerCase().trim() == "app")) {
                        window.location = productWebpage;
						return;
                    }
					
					linkElement.setAttribute("href", productWebpage);					
					linkElement.style.visibility = "visible";
                }
            }catch(err)
            {
                // Link not found. Ignore.
            }
			mainContentElement.style.visibility = "visible";
        }).catch(e => console.log(e));		
    } else {
		mainContentElement.style.visibility = "visible";
	}
}

setProductLink();
