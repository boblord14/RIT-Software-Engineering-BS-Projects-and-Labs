import React from 'react';
import { Component } from 'react';
import './clubs.css';
import { Modal, ModalHeader, ModalBody, ModalFooter, Input } from 'reactstrap';


class AddClub extends Component {
    constructor(props) {
        super(props);

        this.state = {
            name:"",
            music:"",
            city:"",
            yellow:"80",
            max:"100",
            editModal: false
        };
        this.nameChanged = this.nameChanged.bind(this);
        this.musicChanged = this.musicChanged.bind(this);
        this.cityChanged = this.cityChanged.bind(this);
        this.yellowChanged = this.yellowChanged.bind(this);
        this.maxChanged = this.maxChanged.bind(this);
    }

    openModal() {
        this.setState({
            editModal: !this.state.editModal,
        });
    }

    nameChanged(event) {
        this.setState({name: event.target.value});
    }
    musicChanged(event) {
        this.setState({music: event.target.value});
    }
    cityChanged(event) {
        this.setState({city: event.target.value});
    }
    yellowChanged(event) {
        let isnum = /^\d+$/.test(event.target.value);
        if(isnum||(event.target.value==="")){
            this.setState({yellow: event.target.value});  
        }

    }
    maxChanged(event) {
        let isnum = /^\d+$/.test(event.target.value);
        if(isnum||(event.target.value==="")){
            this.setState({max: event.target.value});
        }
    }

    createClub(){
        if((this.state.name.trim()==="")||(this.state.music.trim()==="")||(this.state.city.trim()==="")||(this.state.yellow.trim()==="")||(this.state.max.trim()==="")){
            alert("Please fill in all fields");
        }else{
            this.props.onClick(this.state.name, this.state.yellow, this.state.max, this.state.city, this.state.music);
            this.wipeState();
        }
    }

    wipeState(){
        this.setState({
        editModal: false,
        name:"",
        music:"",
        city:"",
        yellow:"80",
        max:"100"});
    }

    render() {

        return (
            <div className='customContainer'>            
            <button className="addButton" id="newClub" onClick={() => { this.openModal() }}>Add new club</button>
            <Modal isOpen={this.state.editModal} toggle={this.toggleEdit}>
                    <ModalHeader toggle={this.toggleEdit}>Create new club</ModalHeader>
                    <ModalBody>
                    Club Name: <Input placeholder='Club Name' type='text' onChange={this.nameChanged} />
                        Club City: <Input placeholder='Club City' type='text' onChange={this.cityChanged} />
                        Club Music: <Input placeholder='Club Music' type='text' onChange={this.musicChanged} />
                        Club Warning: <Input value={this.state.yellow} placeholder='Club Warning' type='number' onChange={this.yellowChanged} />
                        Club Max: <Input value={this.state.max} placeholder='Club Max' type='number' onChange={this.maxChanged} />
                    </ModalBody>
                    <ModalFooter>
                        <button onClick={() => { this.createClub() }} type="button">Submit</button>
                        <button onClick={() => { this.wipeState() }} type="button">Close</button>
                    </ModalFooter>
                </Modal>
            </div>

        );
    }
}
export default AddClub;