import { Validators } from '@angular/forms';

export const uuidValidator = Validators.pattern(
  '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$'
);

export const amountValidator = Validators.pattern(/^\d+(\.\d{1,2})?$/);
