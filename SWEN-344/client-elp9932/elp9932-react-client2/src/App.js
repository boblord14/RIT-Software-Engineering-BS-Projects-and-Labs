import { Component } from "react";
import './clubs.css';
import Clubbox from "./Clubbox"
import Controls from "./Controls"

class App extends Component
{
  constructor(props) {
    super(props);
    this.updateCounts = this.updateCounts.bind(this);
    this.state = {
      arcaneCount: 0,
      undergroundCount: 0,
      sodaCount: 0,
      studioCount: 0,
    };
  }

  updateCounts(club, direction){
    if(direction === "+"){
      if(club === "arcane") {
        //arcane
        if(this.state.arcaneCount !== 100){
            this.setState({arcaneCount: this.state.arcaneCount+1,})
        }
      }else if(club === "underground") {
        //underground
        if(this.state.undergroundCount !== 50){
          this.setState({undergroundCount: this.state.undergroundCount+1,})
        }
      }else if(club === "soda") {
        //soda
        if(this.state.sodaCount !== 20){
          this.setState({sodaCount: this.state.sodaCount+1,})
        }
      }else if(club === "studio") {
        //studio
        if(this.state.studioCount !== 52){
          this.setState({studioCount: this.state.studioCount+1,})
        }
      }
    } else{
        // eslint-disable-next-line
        if(eval("this.state."+club+"Count != 0")){
        // eslint-disable-next-line
          eval("this.setState({"+club+"Count: this.state."+club+"Count-1,})"); //hacky timesaver. the evals are "safe" because the values i feed to them are preset. warnings suppressed. 
        }
    }
    
  }

    render()
    {
        return(
            <div>
              <h1>Nightclub Capacity</h1>
              <h3>Each time someone enters/ leaves the club, select the correct club and click the appropriate button</h3>
              <div class="flex-container">
              <Clubbox name={"arcane"} count={this.state.arcaneCount} yellow={70} max={100} />
              <Clubbox name={"underground"} count={this.state.undergroundCount} yellow={30} max={50} />
              <Clubbox name={"soda"} count={this.state.sodaCount} yellow={12} max={20}/>
              <Clubbox name={"studio"} count={this.state.studioCount} yellow={32} max={52}/>
              </div>
              <Controls onClick={this.updateCounts}/>
            </div>
            
        );
    }
}
export default App;