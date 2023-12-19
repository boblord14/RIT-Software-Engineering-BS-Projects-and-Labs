import React from 'react';
import { Component } from 'react';
import './clubs.css';
import { Modal, ModalHeader, ModalBody, ModalFooter, Input, Card, CardBody, CardHeader, CardFooter, CardTitle, CardText } from 'reactstrap';

class club {
    constructor(name, count, yellow, max, city, music, id) { //import of class didnt work, too lazy to solve so i just redeclared it
      this.name = name;
      this.count = count;
      this.yellow = yellow;
      this.max = max;
      this.city = city;
      this.music = music;
      this.id = id;
    }
}

class Clubbox extends Component {
    constructor(props) {
        super(props);

        this.state = {
            club: this.props.club,
            clubBackup: this.props.club,
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
        var temp = new club(event.target.value, this.state.club.count, this.state.club.yellow, this.state.club.max, this.state.club.city, this.state.club.music, this.state.club.id);
        this.setState({club: temp});
    }
    musicChanged(event) {
        var temp = new club(this.state.club.name, this.state.club.count, this.state.club.yellow, this.state.club.max, this.state.club.city, event.target.value, this.state.club.id);
        this.setState({club: temp});
    }
    cityChanged(event) {
        var temp = new club(this.state.club.name, this.state.club.count, this.state.club.yellow, this.state.club.max, event.target.value, this.state.club.music, this.state.club.id);
        this.setState({club: temp});
    }
    yellowChanged(event) {
        let isnum = /^\d+$/.test(event.target.value);
        if(isnum||(event.target.value==="")){
            var temp = new club(this.state.club.name, this.state.club.count, event.target.value, this.state.club.max, this.state.club.city, this.state.club.music, this.state.club.id);
            this.setState({club: temp});  
        }

    }
    maxChanged(event) {
        let isnum = /^\d+$/.test(event.target.value);
        if(isnum||(event.target.value==="")){
            var temp = new club(this.state.club.name, this.state.club.count, this.state.club.yellow,event.target.value, this.state.club.city, this.state.club.music, this.state.club.id);
            this.setState({club: temp});
        }
    }

    updateCount(direction) {
        let tempCount;
        if ((direction === '+') && (this.state.club.count < this.state.club.max)) {
            tempCount = new club(this.state.club.name, this.state.club.count+1, this.state.club.yellow, this.state.club.max, this.state.club.city, this.state.club.music, this.state.club.id);
            this.setState({
                club: tempCount
            }, () => {
                this.pushUpdate();
            });

        } else if ((direction === '-') && (this.state.club.count > 0)) {
            tempCount = new club(this.state.club.name, this.state.club.count-1, this.state.club.yellow, this.state.club.max, this.state.club.city, this.state.club.music, this.state.club.id);
            this.setState({
                club: tempCount
            }, () => {
                this.pushUpdate();
            });

        }
    }

    validateEdit(){
        if((this.state.club.name.trim()==="")||(this.state.club.music.trim()==="")||(this.state.club.city.trim()==="")||(this.state.club.yellow==="")||(this.state.max==="")){
            alert("Please fill in all fields. Note that yellow and max must be valid unsigned integers");
        }else{
            this.pushUpdate();
            this.openModal();
        }
    }
    pushUpdate(){
        
        this.props.onUpdate(this.state.club);
        this.setState({ clubBackup: this.state.club });
        
    }

    resetData(){
        this.setState({ club: this.state.clubBackup });
        this.openModal();
    }

    render() {
        if (this.state.club.count === 0) {
            var status = "Empty";
            var color = "LightGreen";
        }
        else if (this.state.club.count < this.state.club.yellow) {
            status = "Welcome!";
            color = "LightGreen";
        } else if (this.state.club.count >= this.state.club.max) {
            color = "Red";
            status = "No one allowed in!";
        } else {
            color = "Yellow";
            status = "Warn the bouncers…";
        }

        var minus;
        var plus;
        if (this.state.club.count <= 0) {
            minus=true;
        } else if(this.state.club.count > 0){
            minus=false;
        }
        if ((this.state.club.count >= this.state.club.max)) {
            plus=true;
        } else if ((this.state.club.count < this.state.club.max)){
            plus=false;
        }

        return (
            <Card className="clubBox" id="box" style={{ backgroundColor: color }}>
                <CardHeader tag="h3">{this.state.club.name}</CardHeader>
                <CardHeader tag="h4">{this.state.club.city}</CardHeader>
                <CardHeader tag="h5">{this.state.club.music}</CardHeader>
                <CardBody>
                    <CardTitle tag="h6">Status: {status}</CardTitle>
                    <CardText className='clubCount'>{this.state.club.count}</CardText>
                    <button className="upButton" type="button" onClick={() => { this.updateCount('+') }} id="addbutton" disabled={plus}>+</button>
                    <button className="downButton" type="button" onClick={() => { this.updateCount('-') }} id="removebutton" disabled={minus}>-</button>
                </CardBody>
                <CardFooter>
                <button className="editButton" type="button" onClick={() => { this.openModal() }} id="editButton">edit club</button>
                <button className="removeButton" type="button" onClick={() => { this.props.onRemoveClick(this.state.club.id) }} id="removeButton">remove club</button>
                </CardFooter>

                <Modal isOpen={this.state.editModal}>
                    <ModalHeader>Edit Data</ModalHeader>
                    <ModalBody>
                        Club Name: <Input value={this.state.club.name} placeholder='Club Name' type='text' onChange={this.nameChanged} />
                        Club City: <Input value={this.state.club.city} placeholder='Club City' type='text' onChange={this.cityChanged} />
                        Club Music: <Input value={this.state.club.music} placeholder='Club Music' type='text' onChange={this.musicChanged} />
                        Club Warning: <Input value={this.state.club.yellow} placeholder='Club Warning' type='number' onChange={this.yellowChanged} />
                        Club Max: <Input value={this.state.club.max} placeholder='Club Max' type='number' onChange={this.maxChanged} />
                    </ModalBody>
                    <ModalFooter>
                         <button onClick={() => {this.validateEdit(); }} type="button">Submit</button>
                         <button onClick={() => { this.resetData() }} type="button">Close</button>
                    </ModalFooter>
                </Modal>
            </Card>
        );
    }
}
export default Clubbox;