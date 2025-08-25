// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
	private LineNumberReader sourceFile = null;
	private String curFileName;
	private ArrayList<Token> curLineTokens = new ArrayList<>();
	private Stack<Integer> indents = new Stack<>();
	private final int TABDIST = 4;

	public Scanner(String fileName) {
		curFileName = fileName;
		indents.push(0);

		try {
			sourceFile = new LineNumberReader(
					new InputStreamReader(
							new FileInputStream(fileName),
							"UTF-8"));

		} catch (IOException e) {
			scannerError("Cannot read " + fileName + "!");
		}
	}

	private void scannerError(String message) {
		String m = "Asp scanner error";
		if (curLineNum() > 0)
			m += " on line " + curLineNum();
		m += ": " + message;

		Main.error(m);
	}

	public Token curToken() {
		while (curLineTokens.isEmpty()) {
			readNextLine();
		}
		return curLineTokens.get(0);
	}

	public void readNextToken() {
		if (!curLineTokens.isEmpty())
			curLineTokens.remove(0);
	}

	private void readNextLine() {
		curLineTokens.clear();

		// Read the next line:
		String line = null;
		try {
			line = sourceFile.readLine();

			// Etter at siste linje er lest: For alle verdier på indents som er > 0,
			// legg et ‘DEDENT’-symbol i curLineTokens.
			if (line == null) {
				for (int i = 0; i < indents.size() - 1; i++) {
					curLineTokens.add(new Token(dedentToken));
				}
				curLineTokens.add(new Token(eofToken));

				sourceFile.close();
				sourceFile = null;

			} else {
				Main.log.noteSourceLine(curLineNum(), line);

				// -- Must be changed in part 1:
				// Håndtering av INDENT/DEDENT, kompendium side41
				// (a) Hvis linjen bare inneholder blanke (og eventuelt en kommentar),ignorere
				if (line.trim().startsWith("#") || line.trim().equals("")) {
					return;
				}
				// (b)Omform alle innledende TAB-er til blanke
				String l = expandLeadingTabs(line);

				// (c)Tell antall innledende blanke n
				int n = findIndent(l);

				if (n > indents.peek()) {
					indents.push(n);
					curLineTokens.add(new Token(indentToken, curLineNum()));
				} else if (n < indents.peek()) {
					indents.pop();
					curLineTokens.add(new Token(dedentToken, curLineNum()));

					boolean indentFeil = true; // anta det er indentfeil
					while (!indents.isEmpty()) {
						if (n == indents.peek()) {
							indentFeil = false;
							break;
						}
						indents.pop();
						curLineTokens.add(new Token(dedentToken, curLineNum()));
					}
					if (indentFeil) {
						scannerError("Indentation error!");
					}
				}
				// Split line l into symbols and add them into curLineTokens
				leggTilTokens(l);
			}

			// Terminate line:
			curLineTokens.add(new Token(newLineToken, curLineNum()));

		} catch (IOException e) {
			sourceFile = null;
			scannerError("Unspecified I/O error!");
		}

		for (Token t : curLineTokens) {
			if (t.kind == eofToken) {
				Main.log.noteToken(t);
				return;
			}
			Main.log.noteToken(t);
		}
	}

	// part 1
	private void leggTilTokens(String s) {
		// Split String s into symbols and add them into curLineTokens
		// \'(.*?)\' -->get 'innhold'
		// \"([^\"]*)\" -->get "",
		// \\W -->get not num or char and only one charac
		// [a-zA-Z]+(\\w)* -->start with char and kan innehlde 0-n char/num
		Pattern pattern = Pattern
				.compile("\'(.*?)\'|\"([^\"]*)\"|[a-zA-Z]+\\$*(\\w)*|[0-9.]+|[<>=!/]+|\\W");
		Matcher matcher = pattern.matcher(s);

		while (matcher.find()) {
			String symbol = matcher.group();
			boolean inTokenKind = false;

			for (TokenKind tk : TokenKind.values()) {
				if (tk.image.equals(symbol)) {
					curLineTokens.add(new Token(tk, curLineNum()));
					inTokenKind = true;
				}
			}
			if (inTokenKind == false) {
				char c = symbol.charAt(0);

				if (isLetterAZ(c)) {
					if (symbol.contains("$")) {
						scannerError("Illegal character: '$'!");
					}
					Token token = new Token(nameToken, curLineNum());
					token.name = symbol;
					curLineTokens.add(token);

				} else if (isDigit(c)) {
					// flyttall -- double
					if (symbol.contains(".")) {
						// ulovly flyttall som .5 eller 3.
						if (symbol.endsWith(".")) {
							scannerError("Illegal character: '.'!");
						}

						Token token = new Token(floatToken, curLineNum());
						token.floatLit = Double.parseDouble(symbol);
						curLineTokens.add(token);
						// heltall -- long
					} else {
						// tall med 0: 0 er ok, men 04 må deles til 0 og 4
						if (symbol.startsWith("0") && symbol.length() > 1) {
							String symbol1 = "0";
							String symbol2 = symbol.substring(1);

							Token token1 = new Token(integerToken, curLineNum());
							token1.integerLit = Long.parseLong(symbol1);
							curLineTokens.add(token1);

							Token token2 = new Token(integerToken, curLineNum());
							token2.integerLit = Long.parseLong(symbol2);
							curLineTokens.add(token2);
						} else {
							Token token = new Token(integerToken, curLineNum());
							token.integerLit = Long.parseLong(symbol);
							curLineTokens.add(token);
						}
					}

				} else if (symbol.startsWith("\"") || symbol.startsWith("\'")) {
					// tekst uten slutt, bare en " eller'
					if (symbol.length() == 1) {
						scannerError("String literal not terminated!");
					}

					Token token = new Token(stringToken, curLineNum());
					token.stringLit = symbol.substring(1, symbol.length() - 1);
					curLineTokens.add(token);
				} else if (symbol.startsWith(".")) {
					scannerError("Illegal character: '.'!");
				}
			}
		}
	}

	public int curLineNum() {
		return sourceFile != null ? sourceFile.getLineNumber() : 0;
	}

	private int findIndent(String s) {
		int indent = 0;

		while (indent < s.length() && s.charAt(indent) == ' ')
			indent++;
		return indent;
	}

	private String expandLeadingTabs(String s) {
		// -- Must be changed in part 1:
		// Omform alle innledende TAB-er til blanke

		String newS = "";
		int n = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == ' ') {
				newS += " ";
				n++;
			} else if (c == '\t') {
				int nReplace = 4 - (n % 4);
				for (int j = 0; j < nReplace; j++) {
					newS += " ";
				}
				n += nReplace;
			} else {
				newS += s.substring(i);
				break;
			}
		}

		// fjerne commentar som ligger på slutten av kode
		if (newS.contains("#") && newS.indexOf("#") != 0) {
			int i = newS.indexOf("#");
			return newS.substring(0, i);
		} else {
			return newS;
		}
	}

	private boolean isLetterAZ(char c) {
		return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || (c == '_');
	}

	private boolean isDigit(char c) {
		return '0' <= c && c <= '9';
	}

	public boolean isCompOpr() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2: > < == >= <= !=
		if (k == greaterToken || k == greaterEqualToken || k == lessToken || k == lessEqualToken
				|| k == notEqualToken || k == doubleEqualToken) {
			return true;
		}
		return false;
	}

	public boolean isFactorPrefix() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2: + -
		if (k == plusToken || k == minusToken) {
			return true;
		}
		return false;
	}

	public boolean isFactorOpr() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2: * / % //
		if (k == astToken || k == slashToken || k == percentToken || k == doubleSlashToken) {
			return true;
		}
		return false;
	}

	public boolean isTermOpr() {
		TokenKind k = curToken().kind;
		// -- Must be changed in part 2: + -
		if (k == plusToken || k == minusToken) {
			return true;
		}
		return false;
	}

	public boolean anyEqualToken() {
		for (Token t : curLineTokens) {
			if (t.kind == equalToken)
				return true;
			if (t.kind == semicolonToken)
				return false;
		}
		return false;
	}
}
