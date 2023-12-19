import React from 'react';
import {Component} from 'react';
import './clubs.css';

class Clubbox extends Component{
    constructor(props) {
        super(props);

        let club;
        if(this.props.name==="arcane"){//just managing the input for the correct name, studio52 is stupid and it breaks the naming scheme lol
            club = "Club Arcane";
        }else if(this.props.name==="underground"){
            club = "Club Underground";
        }else if(this.props.name==="soda"){
            club = "Club Soda";
        }else if(this.props.name==="studio"){
            club = "Studio 52";
        }

        this.state = {
            count: this.props.count,
            name: club,
        };
      }

    componentDidUpdate(prevProps, prevState) {
        if (prevProps.count !== this.props.count) {
            this.setState({
                count: this.props.count,
              });
        }
    }

      render()
      {
        if(this.state.count === 0){
            var status = "";
            var color = "LightGreen";
        }
        else if(this.state.count<this.props.yellow){
            status = "Welcome!";
            color = "LightGreen";
        } else if(this.state.count>=this.props.max){
            color = "Red";
            status = "No one allowed in!";
        } else{
            color = "Yellow";
            status = "Warn the bouncers…";
        }

        return (
            <div>
            <button class="clubBox" id="box" style={{backgroundColor: color}}>{this.state.name}
            <div class="clubStatus" id="status">{status}</div>
            </button>
            <div class="clubCount" name="count">{this.state.count}</div> 
          </div>
        );
      }
}
export default Clubbox;