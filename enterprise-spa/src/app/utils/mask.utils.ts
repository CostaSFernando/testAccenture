export class MaskUtils {
  static onlyNumbers(value: string | null | undefined): string {
    return value ? value.replace(/\D/g, '') : '';
  }

  static cpf(value: string | null | undefined): string {
    const numbers = this.onlyNumbers(value).slice(0, 11);

    return numbers
      .replace(/^(\d{3})(\d)/, '$1.$2')
      .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
      .replace(/^(\d{3})\.(\d{3})\.(\d{3})(\d)/, '$1.$2.$3-$4');
  }

  static cnpj(value: string | null | undefined): string {
    const numbers = this.onlyNumbers(value).slice(0, 14);

    return numbers
      .replace(/^(\d{2})(\d)/, '$1.$2')
      .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
      .replace(/^(\d{2})\.(\d{3})\.(\d{3})(\d)/, '$1.$2.$3/$4')
      .replace(/^(\d{2})\.(\d{3})\.(\d{3})\/(\d{4})(\d)/, '$1.$2.$3/$4-$5');
  }

  static cpfOrCnpj(value: string | null | undefined): string {
    const numbers = this.onlyNumbers(value);

    if (numbers.length <= 11) {
      return this.cpf(numbers);
    }

    return this.cnpj(numbers);
  }

  static rg(value: string | null | undefined): string {
    const numbers = this.onlyNumbers(value).slice(0, 9);

    return numbers
      .replace(/^(\d{2})(\d)/, '$1.$2')
      .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
      .replace(/^(\d{2})\.(\d{3})\.(\d{3})(\d)/, '$1.$2.$3-$4');
  }

  static cep(value: string | null | undefined): string {
    const numbers = this.onlyNumbers(value).slice(0, 8);

    return numbers.replace(/^(\d{5})(\d)/, '$1-$2');
  }
}
