import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpencourtPostComponent } from './opencourt-post.component';

describe('OpencourtPostComponent', () => {
  let component: OpencourtPostComponent;
  let fixture: ComponentFixture<OpencourtPostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OpencourtPostComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OpencourtPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
