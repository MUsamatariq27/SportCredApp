import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpencourtCreateComponent } from './opencourt-create.component';

describe('OpencourtCreateComponent', () => {
  let component: OpencourtCreateComponent;
  let fixture: ComponentFixture<OpencourtCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OpencourtCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OpencourtCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
