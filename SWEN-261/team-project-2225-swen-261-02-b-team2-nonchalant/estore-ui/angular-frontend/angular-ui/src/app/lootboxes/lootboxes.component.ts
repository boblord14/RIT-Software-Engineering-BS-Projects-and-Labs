import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { Lootbox } from '../lootbox';
import { LootboxService } from '../lootbox.service';
import { MessageService } from '../message.service';
import { CurrentUserService } from '../current-user.service';
import { UserService } from '../user.service';
import { Product } from '../product';

@Component({
  selector: 'app-lootboxes',
  templateUrl: './lootboxes.component.html',
  styleUrls: ['./lootboxes.component.css']
})
export class LootboxesComponent implements OnInit {

  selectedLootbox?: Lootbox;

  lootboxes: Lootbox[] = [];

  constructor(private lootboxService: LootboxService, private messageService: MessageService, private currentUserService: CurrentUserService, private userService : UserService) { }

  x= this.currentUserService;

  ngOnInit(): void {
    this.getLootboxes();
  }

  onSelect(lootbox: Lootbox): void {
    this.selectedLootbox = lootbox;
    this.messageService.add(`LootboxesComponent: Selected lootbox id=${lootbox.id}`);
  }

  getLootboxes(): void {
    this.lootboxService.getLootboxes()
        .subscribe(lootboxes => this.lootboxes = lootboxes);
  }

  add(name: string, price: number): void {
    name = name.trim();
    if (!name) { return; }
    if (price <= 0) { return; }

    this.lootboxService.addLootbox({name, price} as Lootbox)
      .subscribe(lootbox => {
        this.lootboxes.push(lootbox);
      });
  }
  delete(lootbox: Lootbox): void {
    this.lootboxes = this.lootboxes.filter(h => h !== lootbox);
    this.lootboxService.deleteLootbox(lootbox.id).subscribe();
  }

}