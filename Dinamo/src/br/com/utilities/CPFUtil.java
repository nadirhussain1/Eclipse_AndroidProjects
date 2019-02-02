package br.com.utilities;

public class CPFUtil {

    /**
     * Returns a formatted <code>String</code> of the given CPF. A fully valid
     * CPF which passed the {@link CPFUtil#validate(String)} test is required.
     * The output matches the format 999.999.999-99.
     * 
     * @param cpf
     *            A full valid CPF.
     * @return Formatted CPF <code>String</code>.
     * @throws IllegalArgumentException
     *             if the given CPF is not valid.
     * @author Jorge Lee
     */
    public static String format(String cpf) {
            if (!validate(cpf)) {
                    throw new IllegalArgumentException("Invalid cpf " + cpf);
            }
            StringBuilder builder = new StringBuilder(cpf.replaceAll("[^\\d]", ""));
            builder.insert(3, '.');
            builder.insert(7, '.');
            builder.insert(11, '-');
            return builder.toString();
    }

    /**
     * Generates a random full valid CPF. The purpose of this method is to help
     * testing procedures by generating valid test data.
     * 
     * @return A full valid CPF <code>String</code>.
     * @author Jorge Lee
     */
    public static String generate() {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 9; i++) {
                    builder.append(Math.round(Math.random() * 9));
            }
            builder.append(getValidationDigits(builder.toString()));
            return builder.toString();
    }

    /**
     * A formatted version of {@link CPFUtil#generate()}. The output is
     * formatted according to {@link CPFUtil#format(String)}.
     * 
     * @return A formatted and full valid CPF <code>String</code>.
     * @author Jorge Lee
     */
    public static String generateFormatted() {
            return format(generate());
    }

    /**
     * Calculate the validation digits based on the government defined method.
     * 
     * @param cpf
     *            A format valid CPF with size between 9 (without validation
     *            digits) and 11 (with validation digits). Existing validation
     *            digits will be ignored.
     * @return A string with two digits.
     * @throws IllegalArgumentException
     *             if input is null or not valid.
     * @author Jorge Lee
     */
    protected static String getValidationDigits(String cpf) {
            if (cpf == null || !cpf.matches("[\\d]{9,11}")) {
                    throw new IllegalArgumentException("CPF is not valid: " + cpf);
            }
            // calculate both digit
            int d1 = 0, d2 = 0;
            for (int i = 0; i < 9; i++) {
                    d1 += Integer.parseInt(cpf.substring(i, i + 1)) * (10 - i);
                    d2 += Integer.parseInt(cpf.substring(i, i + 1)) * (11 - i);
            }
            d1 = 11 - d1 % 11;
            d1 = (d1 > 9) ? 0 : d1;
            // complete using the previous calculated digit
            d2 += d1 * 2;
            d2 = 11 - d2 % 11;
            d2 = (d2 > 9) ? 0 : d2;
            return "" + d1 + d2;
    }

    /**
     * Total validation of the CPF ignoring the format. All non-numeric
     * characters will be ignored. Validation digits are tested as well.
     * 
     * @param cpf
     *            The CPF to be tested.
     * @return true if CPF is full valid.
     * @author Jorge Lee
     */
    public static boolean validate(String cpf) {
            if (cpf == null) {
                    return false;
            }
            String _cpf = cpf.replaceAll("[^\\d]", "");
            if (!_cpf.matches("[\\d]{11}"))
                    return false;
            return _cpf.equals(_cpf.substring(0, 9) + getValidationDigits(_cpf));
    }
}
