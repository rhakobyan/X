function vote(vote){
 var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        if(this.responseText == "voted"){
        if(vote){
            var element = document.getElementById("downVote");
            if(element.classList.contains("voted")){
                element.setAttribute("class", "action-buttons");
            }
            document.getElementById("upVote").setAttribute("class", "voted");
            }
            else{
            var element = document.getElementById("upVote");
                        if(element.classList.contains("voted")){
                            element.setAttribute("class", "action-buttons");
                        }
            document.getElementById("downVote").setAttribute("class", "voted");
            }
        }
        else if(this.responseText == "notVoted"){
            document.getElementById("voteMessage").innerHTML = "You have already voted!";
        }
        else if(this.responseText == "cantVote"){
        document.getElementById("voteMessage").innerHTML = "You must be logged in to vote!";
        }
    }
  };
  xhttp.open("POST", "?vote="+vote+"", true);
  xhttp.send();
}