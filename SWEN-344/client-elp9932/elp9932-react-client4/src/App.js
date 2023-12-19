import { Component } from "react";
import './clubs.css';
import Clubbox from "./Clubbox"
import AddClub from "./AddClub"
import SearchBar from "./SearchBar";
import { Container, Row, Col } from 'reactstrap';

export class club {
  constructor(name, count, yellow, max, city, music,id) {
    this.name = name;
    this.count = count;
    this.yellow = yellow;
    this.max = max;
    this.city = city;
    this.music = music;
    this.id = id;
  }

}

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      clubs: [],
      cityFilter: ""
    };
    this.removeClub = this.removeClub.bind(this);
    this.addClub = this.addClub.bind(this);
    this.filterClubs = this.filterClubs.bind(this);
    this.updateClubs = this.updateClubs.bind(this);
  }

  removeClub(id) {
    this.deleteData(id);
  }

  addClub(name, yellow, max, city, music){
    var newClub = new club(name, 0, yellow, max, city, music, 0);//id doesnt matter here.
    this.createData(newClub);
  }

  filterClubs(city){
    this.setState({
      cityFilter: city
  }, () => {
      this.fetchData();
  });
  }

  updateData = (apiResponse) => {
    var clubList = []
    for(var i = 0;i<apiResponse.length;i++){
      var gettedClub = new club(apiResponse[i][1], apiResponse[i][2], apiResponse[i][3], apiResponse[i][4], apiResponse[i][5], apiResponse[i][6], apiResponse[i][0]);
      clubList.push(gettedClub);
    }
    this.setState({clubs: clubList});
  }
  
  updateClubs(clubInfo){
    this.putData(clubInfo);
  }

  fetchData = () => {
     fetch('http://localhost:5000/club_api?city=' + this.state.cityFilter, {method: 'GET'})
     .then(
         (response) => 
         {
            if (response.status === 200)
            {
               return (response.json()) ;
            }
            else
            {
                console.log("HTTP error:" + response.status + ":" +  response.statusText);
                return ([ ["status ", response.status]]);
            }
         }
         )//The promise response is returned, then we extract the json data
     .then ((jsonOutput) => //jsonOutput now has result of the data extraction
              {
                  this.updateData(jsonOutput);
              }
          )
    .catch((error) => 
            {console.log(error);
                this.updateData("");
             } )
  }

  deleteData = (clubID) => {//NOTE: ON EXAM DO NOT USE BODY PARAMETERS FOR DELETE, USE URL PARAMETERS
    var jsonData = JSON.stringify({id: clubID});
    var hdr = {'content-type': 'application/json'}
    fetch(`http://localhost:5000/club_api`, {method: 'DELETE', body: jsonData, headers: hdr})
    .then(
        (response) => 
        {
           if (response.status !== 200)
           {
               console.log("HTTP error:" + response.status + ":" +  response.statusText);
               return ([ ["status ", response.status]]);
           } else{
            this.fetchData();
           }
        }
        )
   .catch((error) => 
           {console.log(error);} )
 }

 putData = (club) => {
  var jsonData = JSON.stringify({club});
  alert(jsonData)
  var hdr = {'content-type': 'application/json'}
  fetch(`http://localhost:5000/club_api`, {method: 'PUT', body: jsonData, headers: hdr})
  .then(
      (response) => 
      {
         if (response.status !== 200)
         {
             console.log("HTTP error:" + response.status + ":" +  response.statusText);
             return ([ ["status ", response.status]]);
         }
      }
      )
    .then(()=>{this.fetchData();}
      
    )
 .catch((error) => 
         {console.log(error);} )
}

createData = (club) => {
  var jsonData = JSON.stringify({club});
  var hdr = {'content-type': 'application/json'}
  fetch(`http://localhost:5000/club_api`, {method: 'POST', body: jsonData, headers: hdr})
  .then(
      (response) => 
      {
         if (response.status !== 200)
         {
             console.log("HTTP error:" + response.status + ":" +  response.statusText);
             return ([ ["status ", response.status]]);
         } else{
          this.fetchData();
         }
      }
      )
 .catch((error) => 
         {console.log(error);} )
}
  
  componentDidMount(){
    this.fetchData();
  }  

  render() {
    let clubBoxes = [];
    for (var i = 0; i < this.state.clubs.length; i++) {
        clubBoxes.push(<Col xs={6} xl={3} className='colTest' key={this.state.clubs[i].name}><Clubbox key={this.state.clubs[i].name} club={this.state.clubs[i]} onRemoveClick={this.removeClub} onUpdate={this.updateClubs}/></Col>);
    }
    return (
      <div>
        <h1>Nightclub Capacity</h1>
        <h3>Each time someone enters/ leaves the club, select the correct club and click the appropriate button</h3>
        <div className="customContainer"></div>
        <SearchBar onClick={this.filterClubs}/>
        <AddClub onClick={this.addClub}/>
        <Container className="m-6">
        <Row className='rowSize'>{clubBoxes}</Row>
        </Container>
      </div>
    );
  }
}
export default App;