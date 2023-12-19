import React from 'react';
import ReactDOM from 'react-dom';
import {Component} from 'react';
import './style.css';


class Controls extends Component
{
    constructor(props) {
        super(props);
        this.state = {
          counter: 0
        };
        this.doSomething = this.doSomething.bind(this);
      }

    doSomething() {

        alert("Ouch, you clicked me");
        this.setState({counter: this.state.counter+1});  
      }


    render()
    {
        return(
        <div class="flex-container">
                <div class="col1">
                    Here's one thing
                </div>
                <div class="col2">
                    <input type="text" id="id_text" defaultValue="This another thing"></input>
                </div>
                <div class="col3">
                    <button onClick={this.doSomething}>click me</button>
                </div>
                <div id="counter">{this.state.counter}</div>
            </div>
        );
    }

} 

export default Controls;
