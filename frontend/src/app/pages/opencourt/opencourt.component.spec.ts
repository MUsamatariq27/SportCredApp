import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpencourtComponent } from './opencourt.component';

describe('OpencourtComponent', () => {
  let component: OpencourtComponent;
  let fixture: ComponentFixture<OpencourtComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OpencourtComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OpencourtComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
