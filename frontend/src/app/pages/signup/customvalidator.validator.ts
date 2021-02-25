import { FormGroup, AbstractControl } from "@angular/forms";

// To validate password and confirm password
export function ComparePassword(
  controlName: string,
  matchingControlName: string
) {
  return (formGroup: FormGroup) => {
    const control = formGroup.controls[controlName];
    const matchingControl = formGroup.controls[matchingControlName];

    if (matchingControl.errors && !matchingControl.errors.mustMatch) {
      return;
    }

    if (control.value !== matchingControl.value) {
      matchingControl.setErrors({ mustMatch: true });
    } else {
      matchingControl.setErrors(null);
    }
  };
}


  export function agerangeValidate(controlName: string) 
  {

    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];

  
      if (control.errors && !control.errors.ageRange) {
        return;
      }
  
      if (isNaN(+ control.value)== true || Number(control.value) < 10 || Number(control.value) > 90) {
        control.setErrors({ ageRange: true });
      } else {
        control.setErrors(null);
      }
    };

    //|| Number(control.value) < 15 || Number(control.value) > 90

  }