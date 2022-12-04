import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { UserService } from '../core/services/user.service';

@Directive({
  selector: '[appShowAuthed]'
})
export class ShowAuthedDirective {
  constructor(
    private templateRef: TemplateRef<any>,
    private userService: UserService,
    private viewContainer: ViewContainerRef
  ) { }

  condition: boolean | undefined;

  ngOnInit(): void {
    console.log("condition: ", this.condition)
    this.userService.isAuthenticated.subscribe(
      (isAuthenticated) => {
        if (isAuthenticated && this.condition || !isAuthenticated && !this.condition) {
          this.viewContainer.createEmbeddedView(this.templateRef);
        } else {
          this.viewContainer.clear();
        }
      }
    );
  }

  @Input()
  set appShowAuthed(condition: boolean) {
    this.condition = condition;
  }
}
