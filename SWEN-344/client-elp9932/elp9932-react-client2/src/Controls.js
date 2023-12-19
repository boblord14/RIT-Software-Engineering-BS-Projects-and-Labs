import React from 'react';
import {Component} from 'react';
import './clubs.css';

class Controls extends Component
{
    constructor(props) {
        super(props);
        this.state = {
          selected: ""
        };
      }

      handleChange = (event) => {
        this.setState({
            selected: event.target.value,
          });
      }

    render()
    {
        return(
            <div>    
                <div class="container">
                <div class="radio">
                    <div><label><input type="radio" name="clubs" value="arcane" onChange={this.handleChange}></input>Club Arcane</label></div>
                    <div><label><input type="radio" name="clubs" value="underground" onChange={this.handleChange}></input>Club Underground</label></div>
                    <div><label><input type="radio" name="clubs" value="soda" onChange={this.handleChange}></input>Club Soda</label></div>
                    <div><label><input type="radio" name="clubs" value="studio" onChange={this.handleChange}></input>Studio 52</label></div>
                </div>
                </div>
                <div class="container">
                <div><button class="buttons" type="button" onClick={()=>{this.props.onClick(this.state.selected, "+")}}  id="addbutton">+</button></div>
                <div><button class="buttons" type="button"  onClick={()=>{this.props.onClick(this.state.selected, "-")}} id="removebutton">-</button></div>
                </div>
            </div>
        );
    }

} 

export default Controls;
