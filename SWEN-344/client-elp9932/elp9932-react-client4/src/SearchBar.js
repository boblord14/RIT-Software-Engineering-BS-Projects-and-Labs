import React from 'react';
import { Component } from 'react';
import './clubs.css';
import { Input } from 'reactstrap';


class SearchBar extends Component {
    constructor(props) {
        super(props);

        this.state = {
            search:"",
        };
        this.searchChanged = this.searchChanged.bind(this);
    }

    searchChanged(event) {
        this.setState({search: event.target.value});
    }


    searchClub(){
            this.props.onClick(this.state.search);
    }

    render() {

        return (
            <div className='customContainer'> 
            <Input className='searchBar' placeholder='Filter Clubs By City' type='text' onChange={this.searchChanged} />           
            <button id="searchButton" onClick={() => { this.searchClub() }}>Apply filter</button>
            </div>

        );
    }
}
export default SearchBar;