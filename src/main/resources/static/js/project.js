function vote(vote){
 var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        if(this.responseText == "voted"){
        if(vote){
            var element = document.getElementById("downVote");
            if(element.classList.contains("voted") || element.classList.contains("already-voted")){
                element.setAttribute("class", "action-buttons");
            }
            if(document.getElementById("upVote").classList.contains("already-voted") ||document.getElementById("upVote").classList.contains("voted")){
                document.getElementById("upVote").setAttribute("class", "action-buttons");
            }
            else{
            document.getElementById("upVote").setAttribute("class", "voted");
            }
            }
            else{
            var element = document.getElementById("upVote");
                        if(element.classList.contains("voted") || element.classList.contains("already-voted")){
                            element.setAttribute("class", "action-buttons");
                        }
             if(document.getElementById("downVote").classList.contains("already-voted") ||document.getElementById("downVote").classList.contains("voted")){
                document.getElementById("downVote").setAttribute("class", "action-buttons");
                 }

                  else{
                   document.getElementById("downVote").setAttribute("class", "voted");
                   }
            }
             document.getElementById("voteDiv").style.display = "none";
        }
        else if(this.responseText == "CantDownVote"){
            document.getElementById("voteDiv").style.display = "block";
            document.getElementById("voteMessage").innerHTML = "You don't have permission to down vote!";
        }
        else if(this.responseText == "cantVote"){
        document.getElementById("voteDiv").style.display = "block";
        document.getElementById("voteMessage").innerHTML = "You must be logged in to vote!";
        }
    }
  };
  xhttp.open("POST", "?vote="+vote+"", true);
  xhttp.send();
}