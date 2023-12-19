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

    var arcane = new club("Club Arcane", 0,70,100,"Tampa Bay", "Power Metal",0);
    var underground = new club("Club Underground", 0,30,50,"Dundee", "Symphonic Metal",1);
    var soda = new club("Club Soda", 0,12,20,"Antartica", "Melodic Metal",2);
    var studio = new club("Studio 52", 0,32,52,"Principality of Sealand", "Speed Metal",3);
    this.state = {
      clubs: [arcane,underground,soda, studio],
      clubID: 4,
      cityFilter: ""
    };
    this.removeClub = this.removeClub.bind(this);
    this.addClub = this.addClub.bind(this);
    this.filterClubs = this.filterClubs.bind(this);
  }

  removeClub(id) {
    var updatedClubs = [];
    for(var i=0;i<this.state.clubs.length;i++){
      if(this.state.clubs[i].id !== id){
        updatedClubs.push(this.state.clubs[i]);
      }
    }
    this.setState({clubs: updatedClubs});
  }

  addClub(name, yellow, max, city, music){
    var newClub = new club(name, 0, yellow, max, city, music, this.state.clubID);
    var clubList = this.state.clubs;
    clubList.push(newClub);
    this.setState({clubs: clubList,clubID: this.state.clubID+1});
  }

  filterClubs(city){
    this.setState({cityFilter: city});
  }

  render() {
    let clubBoxes = [];
    for (var i = 0; i < this.state.clubs.length; i++) {
      if(this.state.clubs[i].city.toLowerCase().includes(this.state.cityFilter)){
        clubBoxes.push(<Col xs={6} xl={3} className='colTest' key={this.state.clubs[i].name}><Clubbox key={this.state.clubs[i].name} club={this.state.clubs[i]} onRemoveClick={this.removeClub}/></Col>);
      }
      
    }
    return (
      <div>
        <h1>Nightclub Capacity</h1>
        <h3>Each time someone enters/ leaves the club, select the correct club and click the appropriate button

          
        </h3>
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